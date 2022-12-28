package cz.majksa.trask.entrance.dto

import cz.majksa.trask.entrance.CandidateTechnology

/**
 * DTO response class used when listing all technologies of one candidate.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
data class CandidateTechnologyBasic(
    val name: String,
    val level: Int,
    val note: String?,
)

/**
 * DTO response class used when listing all candidates of one technology.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
data class TechnologyCandidateBasic(
    val candidate: CandidateBasic,
    val level: Int,
    val note: String?,
)

/**
 * DTO request class for creating a new candidate-technology binding.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
data class CandidateTechnologyInput(
    val level: Int,
    val note: String?,
)

/**
 * Converts the binding to a DTO used when listing all technologies of one candidate.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
fun CandidateTechnology.withTechnology() = CandidateTechnologyBasic(
    name = technology.name,
    level = level,
    note = note
)

/**
 * Converts the binding to a DTO used when listing all candidates of one technology.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
fun CandidateTechnology.withCandidate() = TechnologyCandidateBasic(
    candidate = candidate.toBasic(),
    level = level,
    note = note
)