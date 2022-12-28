package cz.majksa.trask.entrance

import cz.majksa.trask.entrance.dto.*
import org.springframework.web.bind.annotation.*

@RestController
class CandidatesController(private val repository: CandidatesRepository) {

    /**
     * Returns a list of all candidates.
     *
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @GetMapping("/candidate")
    fun getCandidates(): Iterable<CandidateBasic> = repository.findAll().map { it.toBasic() }

    /**
     * Returns a single candidate by id.
     *
     * @param id id of the requested candidate
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @GetMapping("/candidate/{id}")
    fun getCandidate(@PathVariable id: String): CandidateDetailed = repository.findById(
        id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!")
    ).get().toDetailed()

    /**
     * Creates a new candidate.
     *
     * @param candidate the candidate input
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @PostMapping("/candidate")
    fun createCandidate(@RequestBody candidate: CandidateInput): CandidateBasic =
        repository.save(candidate.toEntity()).toBasic()

    /**
     * Updates a candidate to the new representation received.
     *
     * @param id id of the candidate to be updated
     * @param candidate the candidate input
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @PutMapping("/candidate/{id}")
    fun updateCandidate(@PathVariable id: String, @RequestBody candidate: CandidateInput): CandidateBasic {
        val entity = candidate.toEntity()
        entity.id = id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!")
        return repository.save(entity).toBasic()
    }

    /**
     * Deletes a candidate.
     *
     * @param id id of the candidate to be deleted
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @DeleteMapping("/candidate/{id}")
    fun deleteCandidate(@PathVariable id: String) =
        repository.deleteById(id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!"))
}

@RestController
class TechnologiesController(private val repository: TechnologiesRepository) {

    /**
     * Returns all technologies.
     *
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @GetMapping("/technology")
    fun getTechnologies(): Iterable<TechnologyBasic> = repository.findAll().map { it.toBasic() }

    /**
     * Returns a single technology by id.
     *
     * @param id id of the technology
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @GetMapping("/technology/{id}")
    fun getTechnology(@PathVariable id: String): TechnologyDetailed = repository.findById(
        id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!")
    ).get().toDetailed()

    /**
     * Creates a new technology.
     *
     * @param technology the technology input
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @PostMapping("/technology")
    fun createTechnology(@RequestBody technology: TechnologyInput): TechnologyBasic =
        repository.save(technology.toEntity()).toBasic()

    /**
     * Updates a technology to the new representation received.
     *
     * @param id id of the technology to be updated
     * @param technology the technology input
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @PutMapping("/technology/{id}")
    fun updateTechnology(@PathVariable id: String, @RequestBody technology: TechnologyInput): TechnologyBasic {
        val entity = technology.toEntity()
        entity.id = id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!")
        return repository.save(entity).toBasic()
    }

    /**
     * Deletes a technology.
     *
     * @param id id of the technology to be deleted
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
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

    /**
     * Adds a technology to a candidate. (Creates a new relationship) If the relationship already exists, it is updated.
     *
     * @param candidate id of the candidate
     * @param technology id of the technology
     * @param body the relationship input
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @PostMapping("/candidate/{candidate}/technology/{technology}")
    fun addTechnology(
        @PathVariable candidate: String,
        @PathVariable technology: String,
        @RequestBody body: CandidateTechnologyInput
    ): CandidateDetailed {
        var binding = candidatesTechnologies.findByCandidateIdAndTechnologyId(
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
            // If the relationship does not exist, create it
            binding = candidatesTechnologies.save(
                CandidateTechnology(
                    candidate = candidateEntity,
                    technology = technologyEntity,
                    level = body.level,
                    note = body.note
                )
            )
            binding.candidate.technologies += binding
            binding.technology.candidates += binding
        } else {
            // If the relationship exists, update it
            binding.level = body.level
            binding.note = body.note
            binding.candidate = candidateEntity
            binding.technology = technologyEntity
            candidatesTechnologies.save(binding)
        }
        return binding.candidate.toDetailed()
    }

    /**
     * Removes a technology from a candidate. (Deletes the relationship)
     *
     * @param candidate id of the candidate
     * @param technology id of the technology
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
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

        val binding = candidatesTechnologies.findByCandidateIdAndTechnologyId(
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