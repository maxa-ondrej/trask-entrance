package cz.majksa.trask.entrance

import cz.majksa.trask.entrance.dto.CandidateInput
import cz.majksa.trask.entrance.dto.CandidateTechnologyInput
import cz.majksa.trask.entrance.dto.TechnologyInput
import org.springframework.stereotype.Service

@Service
class CandidateService(private val repository: CandidatesRepository) {

    /**
     * Returns a list of all candidates.
     *
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    fun findAll(): Iterable<Candidate> = repository.findAll()

    /**
     * Returns a single candidate by id.
     *
     * @param id id of the requested candidate
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    fun find(id: Long) = repository.findById(id)

    /**
     * Creates a new candidate.
     *
     * @param candidate the candidate input
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    fun create(candidate: CandidateInput) = repository.save(candidate.toEntity())

    /**
     * Updates a candidate to the new representation received.
     *
     * @param id id of the candidate to be updated
     * @param candidate the candidate input
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    fun update(id: Long, candidate: CandidateInput): Candidate {
        val entity = candidate.toEntity()
        entity.id = id
        return repository.save(entity)
    }

    /**
     * Deletes a candidate.
     *
     * @param id id of the candidate to be deleted
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    fun delete(id: Long): Boolean {
        repository.deleteById(id)
        return true
    }
}

@Service
class TechnologyService(private val repository: TechnologiesRepository) {

    /**
     * Returns all technologies.
     *
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    fun findAll(): Iterable<Technology> = repository.findAll()

    /**
     * Returns a single technology by id.
     *
     * @param id id of the technology
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    fun find(id: Long) = repository.findById(id)

    /**
     * Creates a new technology.
     *
     * @param technology the technology input
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    fun create(technology: TechnologyInput) = repository.save(technology.toEntity())

    /**
     * Updates a technology to the new representation received.
     *
     * @param id id of the technology to be updated
     * @param technology the technology input
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    fun update(id: Long, technology: TechnologyInput): Technology {
        val entity = technology.toEntity()
        entity.id = id
        return repository.save(entity)
    }

    /**
     * Deletes a technology.
     *
     * @param id id of the technology to be deleted
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    fun delete(id: Long): Boolean {
        repository.deleteById(id)
        return true
    }
}

@Service
class CandidatesTechnologiesService(
    private val candidates: CandidatesRepository,
    private val technologies: TechnologiesRepository,
    private val candidatesTechnologies: CandidatesTechnologiesRepository
) {

    /**
     * Adds a technology to a candidate. (Creates a new relationship) If the relationship already exists, it is updated.
     *
     * @param candidateId id of the candidate
     * @param technologyId id of the technology
     * @param body the relationship input
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    fun create(candidateId: Long, technologyId: Long, body: CandidateTechnologyInput): CandidateTechnology {
        var binding = candidatesTechnologies.findByCandidateIdAndTechnologyId(
            candidateId,
            technologyId
        )
        val candidate = candidates.findById(candidateId).get()
        val technology = technologies.findById(technologyId).get()
        if (binding == null) {
            binding = CandidateTechnology(
                candidate = candidate,
                technology = technology,
                level = body.level,
                note = body.note
            )
        } else {
            binding.level = body.level
            binding.note = body.note
            binding.candidate = candidate
            binding.technology = technology
        }
        candidatesTechnologies.save(binding)
        return binding
    }

    /**
     * Removes a technology from a candidate. (Deletes the relationship)
     *
     * @param candidateId id of the candidate
     * @param technologyId id of the technology
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    fun delete(candidateId: Long, technologyId: Long): Boolean {
        val binding = candidatesTechnologies.findByCandidateIdAndTechnologyId(candidateId, technologyId)
        binding?.let {
            candidatesTechnologies.delete(it)
        }
        return true
    }
}