package cz.majksa.trask.entrance

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EntranceApplication

fun main(args: Array<String>) {
    runApplication<EntranceApplication>(*args)
}
