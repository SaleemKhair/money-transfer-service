package org.bsf.transfers.common

import java.math.BigDecimal
import java.time.LocalDateTime

interface MoneyTransfer: java.io.Serializable {
    val uuid: String
    val beneficiary: Account
    val sender: Account
    val amount: BigDecimal
    val issuedAt: LocalDateTime
    val processedAt: LocalDateTime?
    val status: TransactionStatus?
    val id: Long?
}
enum class TransactionStatus {
    PROCESSED,
    PENDING
}
