package cz.majksa.trask.entrance

import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CandidateTechnologyTest {

    @Test
    fun `givenInRange whenSetLevel thenApplyChange`() {
        val candidateTechnology = CandidateTechnology(mockk(), mockk(), 1, null)
        candidateTechnology.level = 2
        assertThat(candidateTechnology.level).isEqualTo(2)
        candidateTechnology.level = 9
        assertThat(candidateTechnology.level).isEqualTo(9)
    }

    @Test
    fun `givenOutOfRange whenSetLevel thenApplyChange`() {
        val candidateTechnology = CandidateTechnology(mockk(), mockk(), 1, null)
        assertThrows<IllegalArgumentException> {
            candidateTechnology.level = 11
        }
    }
}