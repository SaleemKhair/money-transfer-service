package org.bsf.transfers.controllers

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class ApiError() {
    private var timestamp: LocalDateTime
    private lateinit var status: HttpStatus
    private lateinit var message: String
    private lateinit var traceMessage: String
    init {
        this.timestamp = LocalDateTime.now()
    }
    constructor(httpStatus: HttpStatus, throwable: Throwable): this() {
        this.timestamp = LocalDateTime.now()
        this.status = httpStatus
        this.message = "An Error Occurred"
        Companion.error(message, throwable)

    }
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger = LoggerFactory.getLogger(javaClass::class.java)

        fun info(s: String) {
            logger.info(s)
        }

        fun error(s: String, throwable: Throwable? = null) {
            logger.error(s, throwable)
        }

    }

}