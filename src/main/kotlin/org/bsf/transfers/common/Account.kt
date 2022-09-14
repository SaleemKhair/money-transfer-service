package org.bsf.transfers.common

import java.math.BigDecimal

interface Account : java.io.Serializable {
    val iban: String
    val balance: BigDecimal
    val status: AccountStatus?
    val id: Long?
}

enum class AccountStatus {
    ACTIVE,
    INACTIVE
}