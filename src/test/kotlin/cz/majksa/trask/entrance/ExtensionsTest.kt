package cz.majksa.trask.entrance

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ExtensionsTest {

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 5, 8, 10])
    fun `givenNumberInRange whenIntRequiredInRange returnInt`(number: Int) {
        val result = number.requiredInRange(1, 10)
        assertThat(result).isEqualTo(number)
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 0, 11, 12])
    fun `givenNumberNotInRange whenIntRequiredInRange throwException`(number: Int) {
        assertThrows<IllegalArgumentException> {
            number.requiredInRange(1, 10)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["John", "Joe", "Jane", "Josh"])
    fun `givenValidStrings whenStringRequiredNotBlank returnInt`(text: String) {
        val result = text.requiredNotBlank()
        assertThat(result).isEqualTo(text)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "  ", "   "])
    fun `givenEmptyStrings whenStringRequiredNotBlank throwException`(text: String) {
        assertThrows<IllegalArgumentException> {
            text.requiredNotBlank()
        }
    }

}