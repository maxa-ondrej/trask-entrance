package cz.majksa.trask.entrance

import cz.majksa.trask.entrance.dto.TechnologyInput
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
class TechnologyServiceTest {
    private final val repository: TechnologiesRepository = mockk()
    private final val service = TechnologyService(repository)
    private final val technologies: List<Technology> = listOf(
        Technology("kotlin", id = 1),
        Technology("java", id = 2)
    )

    @Test
    fun `whenFindAll thenReturnTechnologies`() {
        // given
        every { repository.findAll() } returns technologies

        // when
        val result = service.findAll()

        // then
        verify(exactly = 1) { repository.findAll() }
        assertThat(result).isEqualTo(technologies)
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2])
    fun `whenFind thenReturnTechnology`(id: Long) {
        // given
        val technology = technologies[id.toInt() - 1]
        every { repository.findById(id) } returns Optional.of(technology)

        // when
        val result = service.find(id)

        // then
        verify(exactly = 1) { repository.findById(id) }
        assertThat(result).isPresent
        assertThat(result).get().isEqualTo(technology)
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
    fun `whenCreate thenReturnTechnology`() {
        // given
        val technology = mockk<TechnologyInput>()
        every { technology.toEntity() } returns technologies[0]
        every { repository.save(technologies[0]) } returns technologies[0]

        // when
        val result = service.create(technology)

        // then
        verify(exactly = 1) {
            repository.save(technologies[0])
        }
        assertThat(result).isEqualTo(technologies[0])
    }

    @Test
    fun `whenUpdate thenReturnTechnology`() {
        // given
        val technology = mockk<TechnologyInput>()
        every { technology.toEntity() } returns technologies[0]
        every { repository.save(technologies[0]) } returns technologies[0]

        // when
        val result = service.update(1, technology)

        // then
        verify(exactly = 1) { repository.save(technologies[0]) }
        assertThat(result).isEqualTo(technologies[0])
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
