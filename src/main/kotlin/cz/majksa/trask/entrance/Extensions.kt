package cz.majksa.trask.entrance

/**
 * Checks if int is in the required range.
 * Example: for a range from 1 to 3, only numbers 1, 2, 3 are acceptable
 *
 * @param from the lower border (inclusive)
 * @param to the upper border (inclusive)
 * @throws IllegalArgumentException if the number is not in the given range
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
fun Int.requiredInRange(from: Int, to: Int) = this.also {
    require(it in from..to) { "Number must be in range $from..$to" }
}

/**
 * Checks if int is in the required range.
 * Example: for a range from 1 to 3, only numbers 1, 2, 3 are acceptable
 *
 * @param from the lower border (inclusive)
 * @param to the upper border (inclusive)
 * @throws IllegalArgumentException if the number is not in the given range
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
fun String.requiredNotBlank() = this.also {
    require(isNotBlank()) { "String must not be empty!" }
}