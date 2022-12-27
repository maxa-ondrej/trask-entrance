package cz.majksa.trask.entrance.dto

import cz.majksa.trask.entrance.Technology

data class TechnologyBasic(
    val id: Long,
    val name: String
)

data class TechnologyDetailed(
    val technology: TechnologyBasic,
    val candidates: List<TechnologyCandidateBasic>
)

data class TechnologyInput(
    val name: String
) {
    fun toEntity() = Technology(
        name = name
    )
}

fun Technology.toBasic() = TechnologyBasic(
    id = id ?: throw IllegalStateException("Technology id is null"),
    name = name
)

fun Technology.toDetailed() = TechnologyDetailed(
    technology = toBasic(),
    candidates = candidates.map { it.withCandidate() }
)