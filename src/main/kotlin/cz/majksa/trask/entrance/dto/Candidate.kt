package cz.majksa.trask.entrance.dto

import cz.majksa.trask.entrance.Candidate

/**
 * DTO response class for a candidate **without** linked technologies.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
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

/**
 * DTO response class for a candidate **with** linked technologies.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
data class CandidateDetailed(
    val candidate: CandidateBasic,
    val technologies: List<CandidateTechnologyBasic>
)

/**
 * DTO request class for creating a new candidate.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
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
    /**
     * Converts the input to database entity.
     *
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
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

/**
 * Converts a candidate entity to a DTO **without** linked technologies.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
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

/**
 * Converts a candidate entity to a DTO **with** linked technologies.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
fun Candidate.toDetailed() = CandidateDetailed(
    candidate = toBasic(),
    technologies = technologies.map { it.withTechnology() }
)