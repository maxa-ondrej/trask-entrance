package cz.majksa.trask.entrance

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.ninjasquad.springmockk.MockkBean
import cz.majksa.trask.entrance.dto.CandidateInput
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
class CandidatesControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var service: CandidateService

    val candidates = listOf(
        Candidate("John", "Doe", "", "", "", "", null, null, id = 1),
        Candidate("Joe", "Doe", "", "", "", "", null, null, id = 2),
    )

    @Test
    fun `whenGetAll thenReturnAllCandidates`() {
        // given
        every { service.findAll() } returns candidates

        // when
        mockMvc.get("/api/candidate").andExpect {
            // then
            status { isOk() }
            content {
                contentType("application/json")
                jsonPath("$[0].name") { value("John") }
                jsonPath("$[1].name") { value("Joe") }
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2])
    fun `givenFound whenGetOne thenReturnCandidate`(id: Long) {
        // given
        every { service.find(id) } returns Optional.of(candidates[id.toInt() - 1])

        // when
        mockMvc.get("/api/candidate/${id}").andExpect {
            // then
            status { isOk() }
            content {
                contentType("application/json")
                jsonPath("$.candidate.name") { value(candidates[id.toInt() - 1].name) }
            }
        }
    }

    @Test
    fun `givenNotFound whenGetOne thenReturnError`() {
        // given
        every { service.find(3) } returns Optional.empty()

        // when
        mockMvc.get("/api/candidate/3").andExpect {
            // then
            status { isNotFound() }
            content { contentType("application/json") }
        }
    }

    @Test
    fun `whenPost thenReturnCandidate`() {
        // given
        val candidateInput = CandidateInput("Josh", "Doe", "", "", "", "", null, null)
        every { service.create(candidateInput) } returns candidateInput.toEntity().also { it.id = 1 }

        // when
        mockMvc.post("/api/candidate") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper().writeValueAsString(candidateInput)
        }.andExpect {
            // then
            status { isOk() }
            content {
                contentType("application/json")
                jsonPath("$.name") { value("Josh") }
                jsonPath("$.surname") { value("Doe") }
            }
        }
    }

    @Test
    fun `whenPut thenReturnCandidate`() {
        // given
        val candidateInput = CandidateInput("Josh", "Doe", "", "", "", "", null, null)
        every { service.update(1, candidateInput) } returns candidateInput.toEntity().also { it.id = 1 }

        // when
        mockMvc.put("/api/candidate/1") {
            contentType = MediaType.APPLICATION_JSON
            content = jsonMapper().writeValueAsString(candidateInput)
        }.andExpect {
            // then
            status { isOk() }
            content {
                contentType("application/json")
                jsonPath("$.name") { value("Josh") }
                jsonPath("$.surname") { value("Doe") }
            }
        }
    }

    @Test
    fun `whenDelete thenReturnTrue`() {
        // given
        every { service.delete(1) } returns true

        // when
        mockMvc.delete("/api/candidate/1").andExpect {
            // then
            status { isOk() }
            content {
                contentType("application/json")
                jsonPath("$.success") { value(true) }
            }
        }
    }


}