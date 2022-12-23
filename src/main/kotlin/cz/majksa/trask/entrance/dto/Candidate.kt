package cz.majksa.trask.entrance.dto

import cz.majksa.trask.entrance.Candidate
import java.time.LocalDateTime

data class CandidateBasic(
    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val birth: LocalDateTime,
    val city: String,
    val country: String,
    val cv: String,
    val linkedin: String?,
    val github: String?
)

data class CandidateDetailed(
    val candidate: CandidateBasic,
    val technologies: List<CandidateTechnologyBasic>
)

fun Candidate.toBasic() = CandidateBasic(
    id = id ?: throw IllegalStateException("Candidate id is null"),
    name = name,
    surname = surname,
    email = email,
    phone = phone,
    birth = birth,
    city = city,
    country = country,
    cv = cv,
    linkedin = linkedin,
    github = github
)

fun Candidate.toDetailed() = CandidateDetailed(
    candidate = toBasic(),
    technologies = technologies.map { it.withTechnology() }
)