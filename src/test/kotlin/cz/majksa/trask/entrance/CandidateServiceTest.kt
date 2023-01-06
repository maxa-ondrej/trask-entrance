package cz.majksa.trask.entrance

import cz.majksa.trask.entrance.dto.CandidateInput
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class CandidateServiceTest {
    private final val repository: CandidatesRepository = mockk()
    private final val service = CandidateService(repository)
    private final val candidates: List<Candidate> = listOf(
        Candidate("John", "Doe", "", "", "", "", null, null, id = 1),
        Candidate("Joe", "Doe", "", "", "", "", null, null, id = 2)
    )

    @Test
    fun `whenFindAll thenReturnCandidates`() {
        // given
        every { repository.findAll() } returns candidates

        // when
        val result = service.findAll()

        // then
        verify(exactly = 1) { repository.findAll() }
        assertThat(result).isEqualTo(candidates)
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2])
    fun `whenFind thenReturnCandidate`(id: Long) {
        // given
        val candidate = candidates[id.toInt() - 1]
        every { repository.findById(id) } returns Optional.of(candidate)

        // when
        val result = service.find(id)

        // then
        verify(exactly = 1) { repository.findById(id) }
        assertThat(result).isPresent
        assertThat(result).get().isEqualTo(candidate)
    }

    @Test
    fun `whenFind thenReturnNull`() {
        // given
        every { repository.findById(0) } returns Optional.empty()

        // when
        val result = service.find(0)

        // then
        verify(exactly = 1) { repository.findById(0) }
        assertThat(result).isEmpty
    }

    @Test
    fun `whenCreate thenReturnCandidate`() {
        // given
        val candidate = mockk<CandidateInput>()
        every { candidate.toEntity() } returns candidates[0]
        every { repository.save(candidates[0]) } returns candidates[0]

        // when
        val result = service.create(candidate)

        // then
        verify(exactly = 1) { repository.save(candidates[0]) }
        assertThat(result).isEqualTo(candidates[0])
    }

    @Test
    fun `whenUpdate thenReturnCandidate`() {
        // given
        val candidate = mockk<CandidateInput>()
        every { candidate.toEntity() } returns candidates[0]
        every { repository.save(candidates[0]) } returns candidates[0]

        // when
        val result = service.update(1, candidate)

        // then
        verify(exactly = 1) { repository.save(candidates[0]) }
        assertThat(result).isEqualTo(candidates[0])
    }

    @Test
    fun `whenDelete thenReturnTrue`() {
        // given
        every { repository.deleteById(1) } returns Unit

        // when
        val result = service.delete(1)

        // then
        verify(exactly = 1) { repository.deleteById(1) }
        assertThat(result).isEqualTo(true)
    }
}
