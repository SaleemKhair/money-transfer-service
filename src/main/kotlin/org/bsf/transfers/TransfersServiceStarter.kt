package org.bsf.transfers

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class TransfersServiceStarter

fun main(args: Array<String>) {
    runApplication<TransfersServiceStarter>(*args)
}
