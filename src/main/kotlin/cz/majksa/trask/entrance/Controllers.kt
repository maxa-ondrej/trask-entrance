package cz.majksa.trask.entrance

import cz.majksa.trask.entrance.dto.*
import org.springframework.web.bind.annotation.*

@RestController
class CandidatesController(private val repository: CandidatesRepository) {

    @GetMapping("/candidate")
    fun getCandidates(): Iterable<CandidateBasic> = repository.findAll().map { it.toBasic() }

    @GetMapping("/candidate/{id}")
    fun getCandidate(@PathVariable id: String): CandidateDetailed = repository.findById(
        id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!")
    ).get().toDetailed()

    @PostMapping("/candidate")
    fun createCandidate(@RequestBody candidate: Candidate): CandidateBasic = repository.save(candidate).toBasic()

    @PutMapping("/candidate/{id}")
    fun updateCandidate(@PathVariable id: String, @RequestBody candidate: Candidate): CandidateBasic {
        candidate.id = id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!")
        return repository.save(candidate).toBasic()
    }

    @DeleteMapping("/candidate/{id}")
    fun deleteCandidate(@PathVariable id: String) =
        repository.deleteById(id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!"))
}

@RestController
class TechnologiesController(private val repository: TechnologiesRepository) {

    @GetMapping("/technology")
    fun getTechnologies(): Iterable<TechnologyBasic> = repository.findAll().map { it.toBasic() }

    @GetMapping("/technology/{id}")
    fun getTechnology(@PathVariable id: String): TechnologyDetailed = repository.findById(
        id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!")
    ).get().toDetailed()

    @PostMapping("/technology")
    fun createTechnology(@RequestBody technology: Technology): TechnologyBasic = repository.save(technology).toBasic()

    @PutMapping("/technology/{id}")
    fun updateTechnology(@PathVariable id: String, @RequestBody technology: Technology): TechnologyBasic {
        technology.id = id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!")
        return repository.save(technology).toBasic()
    }

    @DeleteMapping("/technology/{id}")
    fun deleteTechnology(@PathVariable id: String) =
        repository.deleteById(id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!"))
}

@RestController
class CandidatesTechnologiesController(
    private val candidates: CandidatesRepository,
    private val technologies: TechnologiesRepository,
    private val candidatesTechnologies: CandidatesTechnologiesRepository
) {

    @PostMapping("/candidate/{candidate}/technology/{technology}")
    fun addTechnology(
        @PathVariable candidate: String,
        @PathVariable technology: String,
        @RequestBody body: CandidateTechnologyInput
    ): CandidateDetailed {
        var binding = candidatesTechnologies.findByCandidateTechnologyByCandidateIdAndTechnologyId(
            candidate.toLongOrNull() ?: throw IllegalArgumentException("Candidate id must be a number!"),
            technology.toLongOrNull() ?: throw IllegalArgumentException("Technology id must be a number!")
        )
        val candidateEntity = candidates.findById(
            candidate.toLongOrNull() ?: throw IllegalArgumentException("Candidate id must be a number!")
        ).get()
        val technologyEntity = technologies.findById(
            technology.toLongOrNull() ?: throw IllegalArgumentException("Technology id must be a number!")
        ).get()
        if (binding == null) {
            binding = candidatesTechnologies.save(CandidateTechnology(
                candidate = candidateEntity,
                technology = technologyEntity,
                level = body.level,
                note = body.note
            ))
            binding.candidate.technologies += binding
            binding.technology.candidates += binding
        } else {
            binding.level = body.level
            binding.note = body.note
            binding.candidate = candidateEntity
            binding.technology = technologyEntity
            candidatesTechnologies.save(binding)
        }
        return binding.candidate.toDetailed()
    }

    @DeleteMapping("/candidate/{candidate}/technology/{technology}")
    fun removeTechnology(
        @PathVariable candidate: String,
        @PathVariable technology: String
    ): CandidateDetailed {
        val candidateEntity = candidates.findById(
            candidate.toLongOrNull() ?: throw IllegalArgumentException("Candidate id must be a number!")
        ).get()
        val technologyEntity = technologies.findById(
            technology.toLongOrNull() ?: throw IllegalArgumentException("Technology id must be a number!")
        ).get()
        val binding = candidatesTechnologies.findByCandidateTechnologyByCandidateIdAndTechnologyId(
            candidateEntity.id ?: throw IllegalArgumentException("Candidate id must be a number!"),
            technologyEntity.id ?: throw IllegalArgumentException("Technology id must be a number!")
        )
        binding?.let {
            candidateEntity.technologies -= it
            technologyEntity.candidates -= it
            candidatesTechnologies.delete(it)
        }

        return candidateEntity.toDetailed()
    }

}