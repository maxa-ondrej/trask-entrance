package cz.majksa.trask.entrance

import cz.majksa.trask.entrance.dto.*
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.NoSuchElementException

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class EntranceControllerAdvice {

    @ExceptionHandler(NoSuchElementException::class)
    @ResponseBody
    @ResponseStatus
    fun handleNoSuchElementException(exception: NoSuchElementException) = response(404, exception.message)

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseBody
    @ResponseStatus
    fun handleIllegalArgumentException(exception: IllegalArgumentException) = response(403, exception.message)

    fun response(code: Int, message: String?) = ResponseEntity(
        mapOf("message" to Optional.ofNullable(message).orElse("No message provided")), HttpStatusCode.valueOf(code)
    )
}

@RestController
@RequestMapping("/api/candidate")
class CandidatesController(private val service: CandidateService) {

    /**
     * Returns a list of all candidates.
     *
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @GetMapping
    fun getCandidates() = service.findAll().map { it.toBasic() }

    /**
     * Returns a single candidate by id.
     *
     * @param id id of the requested candidate
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @GetMapping("{id}")
    fun getCandidate(@PathVariable id: String) = service.find(
        id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!")
    ).get().toDetailed()

    /**
     * Creates a new candidate.
     *
     * @param candidate the candidate input
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @PostMapping
    fun createCandidate(@RequestBody candidate: CandidateInput) =
        service.create(candidate).toBasic()

    /**
     * Updates a candidate to the new representation received.
     *
     * @param id id of the candidate to be updated
     * @param candidate the candidate input
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @PutMapping("{id}")
    fun updateCandidate(@PathVariable id: String, @RequestBody candidate: CandidateInput) = service.update(
        id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!"),
        candidate
    )

    /**
     * Deletes a candidate.
     *
     * @param id id of the candidate to be deleted
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @DeleteMapping("{id}")
    fun deleteCandidate(@PathVariable id: String) = mapOf(
        "success" to service.delete(id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!"))
    )
}

@RestController
@RequestMapping("/api/technology")
class TechnologiesController(private val service: TechnologyService) {

    /**
     * Returns all technologies.
     *
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @GetMapping
    fun getTechnologies() = service.findAll().map { it.toBasic() }

    /**
     * Returns a single technology by id.
     *
     * @param id id of the technology
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @GetMapping("{id}")
    fun getTechnology(@PathVariable id: String) = service.find(
        id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!")
    ).get().toDetailed()

    /**
     * Creates a new technology.
     *
     * @param technology the technology input
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @PostMapping
    fun createTechnology(@RequestBody technology: TechnologyInput) = service.create(technology).toBasic()

    /**
     * Updates a technology to the new representation received.
     *
     * @param id id of the technology to be updated
     * @param technology the technology input
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @PutMapping("{id}")
    fun updateTechnology(@PathVariable id: String, @RequestBody technology: TechnologyInput) = service.update(
        id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!"),
        technology
    ).toBasic()

    /**
     * Deletes a technology.
     *
     * @param id id of the technology to be deleted
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @DeleteMapping("{id}")
    fun deleteTechnology(@PathVariable id: String) = mapOf(
        "success" to service.delete(id.toLongOrNull() ?: throw IllegalArgumentException("Id must be a number!"))
    )
}

@RestController
@RequestMapping("/candidate/{candidate}/technology/{technology}")
class CandidatesTechnologiesController(private val service: CandidatesTechnologiesService) {

    /**
     * Adds a technology to a candidate. (Creates a new relationship) If the relationship already exists, it is updated.
     *
     * @param candidate id of the candidate
     * @param technology id of the technology
     * @param body the relationship input
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @PostMapping
    fun addTechnology(
        @PathVariable candidate: String,
        @PathVariable technology: String,
        @RequestBody body: CandidateTechnologyInput
    ) = service.create(
        candidate.toLongOrNull() ?: throw IllegalArgumentException("Candidate id must be a number!"),
        technology.toLongOrNull() ?: throw IllegalArgumentException("Technology id must be a number!"),
        body
    ).candidate.toDetailed()

    /**
     * Removes a technology from a candidate. (Deletes the relationship)
     *
     * @param candidate id of the candidate
     * @param technology id of the technology
     * @version 1.0.0
     * @since 1.0.0
     * @author Ondřej Maxa
     */
    @DeleteMapping
    fun removeTechnology(
        @PathVariable candidate: String,
        @PathVariable technology: String
    ) = mapOf(
        "success" to service.delete(
            candidate.toLongOrNull() ?: throw IllegalArgumentException("Candidate id must be a number!"),
            technology.toLongOrNull() ?: throw IllegalArgumentException("Technology id must be a number!")
        )
    )

}