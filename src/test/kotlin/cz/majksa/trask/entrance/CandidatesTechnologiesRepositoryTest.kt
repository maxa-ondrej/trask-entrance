package cz.majksa.trask.entrance

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class CandidatesTechnologiesRepositoryTest {

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var repository: CandidatesTechnologiesRepository

    @Test
    fun `whenFindByCandidateIdAndTechnologyId thenReturnBinding`() {
        // given
        val candidate = Candidate("John", "Doe", "", "", "", "", null, null)
        val technology = Technology("Java")
        val candidateTechnology = CandidateTechnology(candidate, technology, 1, null)

        entityManager.persist(candidate)
        entityManager.persist(technology)
        entityManager.persist(candidateTechnology)

        // when
        val result = repository.findByCandidateIdAndTechnologyId(1, 1)
        val result2 = repository.findByCandidateIdAndTechnologyId(2, 2)

        // then
        assertThat(result).isNotNull
        assertThat(result2).isNull()
        assertThat(result?.candidate).isEqualTo(candidate)
        assertThat(result?.technology).isEqualTo(technology)
    }
}