package cz.majksa.trask.entrance

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface
CandidatesRepository : CrudRepository<Candidate, Long>
interface TechnologiesRepository : CrudRepository<Technology, Long>
interface CandidatesTechnologiesRepository : CrudRepository<CandidateTechnology, Long> {
    /**
     * Searches for an existing binding between a candidate and a technology.
     *
     * @param candidate id of the candidate
     * @param technology id of the technology
     * @return The existing binding or `null`
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @Query("SELECT ct FROM CandidateTechnology AS ct WHERE ct.candidate.id = :candidate AND ct.technology.id = :technology")
    fun findByCandidateIdAndTechnologyId(
        @Param("candidate") candidate: Long,
        @Param("technology") technology: Long
    ): CandidateTechnology?
}