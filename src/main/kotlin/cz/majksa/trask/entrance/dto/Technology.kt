package cz.majksa.trask.entrance.dto

import cz.majksa.trask.entrance.Technology

/**
 * DTO response class for a technology **without** linked candidates.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
data class TechnologyBasic(
    val id: Long,
    val name: String
)

/**
 * DTO response class for a technology **with** linked candidates.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
data class TechnologyDetailed(
    val technology: TechnologyBasic,
    val candidates: List<TechnologyCandidateBasic>
)

/**
 * DTO request class for creating a new technology.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
data class TechnologyInput(
    val name: String
) {
    /**
     * Converts the input to database entity.
     *
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    fun toEntity() = Technology(
        name = name
    )
}

/**
 * Converts a technology entity to a DTO **without** linked candidates.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
fun Technology.toBasic() = TechnologyBasic(
    id = id ?: throw IllegalStateException("Technology id is null"),
    name = name
)

/**
 * Converts a technology entity to a DTO **with** linked candidates.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
fun Technology.toDetailed() = TechnologyDetailed(
    technology = toBasic(),
    candidates = candidates.map { it.withCandidate() }
)