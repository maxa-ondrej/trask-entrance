package cz.majksa.trask.entrance

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.ninjasquad.springmockk.MockkBean
import cz.majksa.trask.entrance.dto.TechnologyInput
import io.mockk.every
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TechnologiesControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var service: TechnologyService

    val technologies = listOf(
        Technology("Kotlin", id = 1),
        Technology("Java", id = 2),
    )

    @Test
    fun `whenGetAll thenReturnAllTechnologies`() {
        // given
        every { service.findAll() } returns technologies

        // when
        mockMvc.get("/api/technology").andExpect {
            // then
            status { isOk() }
            content {
                contentType("application/json")
                jsonPath("$[0].name") { value("Kotlin") }
                jsonPath("$[1].name") { value("Java") }
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2])
    fun `givenFound whenGetOne thenReturnTechnology`(id: Long) {
        // given
        every { service.find(id) } returns Optional.of(technologies[id.toInt() - 1])

        // when
        mockMvc.get("/api/technology/${id}").andExpect {
            // then
            status { isOk() }
            content {
                contentType("application/json")
                jsonPath("$.technology.name") { value(technologies[id.toInt() - 1].name) }
            }
        }
    }

    @Test
    fun `givenNotFound whenGetOne thenReturnError`() {
        // given
        every { service.find(3) } returns Optional.empty()

        // when
        mockMvc.get("/api/technology/3").andExpect {
            // then
            status { isNotFound() }
            content { contentType("application/json") }
        }
    }

    @Test
    fun `whenPost thenReturnTechnology`() {
        // given
        val technologyInput = TechnologyInput("Rust")
        every { service.create(technologyInput) } returns technologyInput.toEntity().also { it.id = 1 }

        // when
        mockMvc.post("/api/technology") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper().writeValueAsString(technologyInput)
        }.andExpect {
            // then
            status { isOk() }
            content {
                contentType("application/json")
                jsonPath("$.name") { value("Rust") }
            }
        }
    }

    @Test
    fun `whenPut thenReturnTechnology`() {
        // given
        val technologyInput = TechnologyInput("Rust")
        every { service.update(1, technologyInput) } returns technologyInput.toEntity().also { it.id = 1 }

        // when
        mockMvc.put("/api/technology/1") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper().writeValueAsString(technologyInput)
        }.andExpect {
            // then
            status { isOk() }
            content {
                contentType("application/json")
                jsonPath("$.name") { value("Rust") }
            }
        }
    }

    @Test
    fun `whenDelete thenReturnTrue`() {
        // given
        every { service.delete(1) } returns true

        // when
        mockMvc.delete("/api/technology/1").andExpect {
            // then
            status { isOk() }
            content {
                contentType("application/json")
                jsonPath("$.success") { value(true) }
            }
        }
    }


}