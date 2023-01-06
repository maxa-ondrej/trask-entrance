package cz.majksa.trask.entrance

import cz.majksa.trask.entrance.dto.toDetailed
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DTOMethodsTest {
    val candidates = listOf(
        Candidate("John", "Doe", "", "", "", "", null, null, id = 1),
        Candidate("Joe", "Doe", "", "", "", "", null, null, id = 2),
    )

    val technologies = listOf(
        Technology("Java", id = 1),
        Technology("Kotlin", id = 2),
    )

    @Test
    fun `whenDetailed returnDetailedDTO`() {
        // given
        val binding = CandidateTechnology(candidates[0], technologies[0], 1, null)
        candidates[0].technologies += binding
        technologies[0].candidates += binding

        // when
        val detailedCandidate = candidates[0].toDetailed()
        val detailedTechnology = technologies[0].toDetailed()

        // then
        assertThat(detailedCandidate.candidate.name).isEqualTo(candidates[0].name)
        assertThat(detailedCandidate.technologies[0].name).isEqualTo(candidates[0].technologies.first().technology.name)
        assertThat(detailedTechnology.technology.name).isEqualTo(technologies[0].name)
        assertThat(detailedTechnology.candidates[0].candidate.name).isEqualTo(technologies[0].candidates.first().candidate.name)
    }
}