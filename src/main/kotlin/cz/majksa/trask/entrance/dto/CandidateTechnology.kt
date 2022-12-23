package cz.majksa.trask.entrance.dto

import cz.majksa.trask.entrance.CandidateTechnology

data class CandidateTechnologyBasic(
    val name: String,
    val level: Int,
    val note: String?,
)

data class TechnologyCandidateBasic(
    val candidate: CandidateBasic,
    val level: Int,
    val note: String?,
)

data class CandidateTechnologyInput(
    val level: Int,
    val note: String?,
)

fun CandidateTechnology.withTechnology() = CandidateTechnologyBasic(
    name = technology.name,
    level = level,
    note = note
)

fun CandidateTechnology.withCandidate() = TechnologyCandidateBasic(
    candidate = candidate.toBasic(),
    level = level,
    note = note
)