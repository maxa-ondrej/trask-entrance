package cz.majksa.trask.entrance

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EntranceApplication

/**
 * The entrance point to the REST API.
 *
 * @param args arguments received from console
 * @version 1.0.0
 * @since 1.0.0
 * @author Ondřej Maxa
 */
fun main(args: Array<String>) {
    runApplication<EntranceApplication>(*args)
}
