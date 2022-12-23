package cz.majksa.trask.entrance

fun Int.requiredInRange(from: Int, to: Int) = this.also {
    require(it in from..to) { "Number must be in range $from..$to" }
}