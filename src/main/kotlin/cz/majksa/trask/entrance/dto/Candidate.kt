package cz.majksa.trask.entrance.dto

import cz.majksa.trask.entrance.Candidate

data class CandidateBasic(
    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val city: String,
    val country: String,
    val linkedin: String?,
    val github: String?
)

data class CandidateDetailed(
    val candidate: CandidateBasic,
    val technologies: List<CandidateTechnologyBasic>
)

data class CandidateInput(
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val city: String,
    val country: String,
    val linkedin: String?,
    val github: String?
) {
    fun toEntity() = Candidate(
        name = name,
        surname = surname,
        email = email,
        phone = phone,
        city = city,
        country = country,
        linkedin = linkedin,
        github = github
    )
}

fun Candidate.toBasic() = CandidateBasic(
    id = id ?: throw IllegalStateException("Candidate id is null"),
    name = name,
    surname = surname,
    email = email,
    phone = phone,
    city = city,
    country = country,
    linkedin = linkedin,
    github = github
)

fun Candidate.toDetailed() = CandidateDetailed(
    candidate = toBasic(),
    technologies = technologies.map { it.withTechnology() }
)