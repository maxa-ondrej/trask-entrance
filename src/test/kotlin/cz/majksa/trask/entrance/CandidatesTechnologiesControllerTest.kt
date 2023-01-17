package cz.majksa.trask.entrance

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.ninjasquad.springmockk.MockkBean
import cz.majksa.trask.entrance.dto.CandidateTechnologyInput
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class CandidatesTechnologiesControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var service: CandidatesTechnologiesService

    val candidates = listOf(
        Candidate("John", "Doe", "", "", "", "", null, null, id = 1),
        Candidate("Joe", "Doe", "", "", "", "", null, null, id = 2),
    )

    val technologies = listOf(
        Technology("Java", id = 1),
        Technology("Kotlin", id = 2),
    )

    @Test
    fun `whenCreate thenReturnBinding`() {
        // given
        val input = CandidateTechnologyInput(3, null)
        val binding = CandidateTechnology(candidates[0], technologies[1], input.level, input.note, id = 1)
        candidates[0].technologies += binding
        technologies[1].candidates += binding
        every { service.create(1, 2, input) } returns binding

        // when
        mockMvc.post("/api/candidate/1/technology/2") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper().writeValueAsString(input)
        }.andExpect {
            // then
            status { isOk() }
            content {
                contentType("application/json")
                jsonPath("$.candidate.name") { value("John") }
                jsonPath("$.technologies[0].technology.name") { value("Kotlin") }
            }
        }
    }

    @Test
    fun `givenInvalidRange whenCreate thenReturnBinding`() {
        // given
        val input = CandidateTechnologyInput(11, null)
        every { service.create(1, 2, input) } throws IllegalArgumentException("Level must be between 1 and 10")

        // when
        mockMvc.post("/api/candidate/1/technology/2") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper().writeValueAsString(input)
        }.andExpect {
            // then
            status { isBadRequest() }
            content { contentType("application/json") }
        }
    }

    @Test
    fun `whenDelete thenReturnTrue`() {
        // given
        every { service.delete(1, 2) } returns true

        // when
        mockMvc.delete("/api/candidate/1/technology/2").andExpect {
            // then
            status { isOk() }
            content {
                contentType("application/json")
                jsonPath("$.success") { value(true) }
            }
        }
    }

}