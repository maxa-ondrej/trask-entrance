package cz.majksa.trask.entrance

import cz.majksa.trask.entrance.dto.CandidateTechnologyInput
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class CandidatesTechnologiesServiceTest {
    private final val candidatesRepository: CandidatesRepository = mockk()
    private final val technologiesRepository: TechnologiesRepository = mockk()
    private final val candidatesTechnologiesRepository: CandidatesTechnologiesRepository = mockk()
    private final val service =
        CandidatesTechnologiesService(candidatesRepository, technologiesRepository, candidatesTechnologiesRepository)

    private final val candidates = listOf(
        Candidate("John", "Doe", "", "", "", "", null, null, id = 1),
        Candidate("Joe", "Doe", "", "", "", "", null, null, id = 2)
    )
    private final val technologies = listOf(
        Technology("Java", id = 1),
        Technology("Kotlin", id = 2)
    )

    @Test
    fun `whenCreate thenReturnBinding`() {
        // given
        val input = CandidateTechnologyInput(3, null)
        val binding = CandidateTechnology(candidates[0], technologies[0], input.level, input.note, id = 1)
        every { candidatesRepository.findById(1) } returns Optional.of(candidates[0])
        every { technologiesRepository.findById(1) } returns Optional.of(technologies[0])
        every { candidatesTechnologiesRepository.findByCandidateIdAndTechnologyId(1, 1) } returns binding
        every { candidatesTechnologiesRepository.save(binding) } returns binding

        // when
        val result = service.create(1, 1, input)

        // then
        assertThat(result).isEqualTo(binding)
        verify(exactly = 1) { candidatesRepository.findById(1) }
        verify(exactly = 1) { technologiesRepository.findById(1) }
        verify(exactly = 1) { candidatesTechnologiesRepository.findByCandidateIdAndTechnologyId(1, 1) }
        verify(exactly = 1) { candidatesTechnologiesRepository.save(binding) }
    }

    @Test
    fun `givenInvalidRange whenCreate thenReturnBinding`() {
        // given
        val input = CandidateTechnologyInput(11, null)
        every { candidatesRepository.findById(1) } returns Optional.of(candidates[0])
        every { technologiesRepository.findById(1) } returns Optional.of(technologies[0])
        every { candidatesTechnologiesRepository.findByCandidateIdAndTechnologyId(1, 1) } returns null

        assertThrows<IllegalArgumentException> {
            service.create(1, 1, input)
        }
    }

    @Test
    fun `whenDelete thenReturnTrue`() {
        // given
        val binding = CandidateTechnology(candidates[0], technologies[0], 3, null, id = 1)
        every { candidatesTechnologiesRepository.findByCandidateIdAndTechnologyId(1, 1) } returns binding
        every { candidatesTechnologiesRepository.delete(binding) } returns Unit

        // when
        val result = service.delete(1, 1)

        // then
        assertThat(result).isEqualTo(true)
        verify(exactly = 1) { candidatesTechnologiesRepository.findByCandidateIdAndTechnologyId(1, 1) }
        verify(exactly = 1) { candidatesTechnologiesRepository.delete(binding) }
    }
}
