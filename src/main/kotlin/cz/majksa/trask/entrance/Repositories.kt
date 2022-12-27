package cz.majksa.trask.entrance

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface CandidatesRepository : CrudRepository<Candidate, Long>
interface TechnologiesRepository : CrudRepository<Technology, Long>
interface CandidatesTechnologiesRepository : CrudRepository<CandidateTechnology, Long> {

    @Query("SELECT ct FROM CandidateTechnology AS ct WHERE ct.candidate.id = :candidate AND ct.technology.id = :technology")
    fun findByCandidateIdAndTechnologyId(
        @Param("candidate") candidate: Long,
        @Param("technology") technology: Long
    ): CandidateTechnology?
}