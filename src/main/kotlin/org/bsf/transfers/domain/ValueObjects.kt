package org.bsf.transfers.domain

import org.bsf.transfers.common.*
import java.math.BigDecimal
import java.time.LocalDateTime

data class MoneyTransferView(
    override val beneficiary: AccountView,
    override val sender: AccountView,
    override val amount: BigDecimal,
    override val issuedAt: LocalDateTime = LocalDateTime.now(),
    override val processedAt: LocalDateTime,
    override val status: TransactionStatus,
    override val uuid: String,
    override val id: Long? = null
): MoneyTransfer

data class AccountView (
    override val iban: String,
    override val balance: BigDecimal,
    override val status: AccountStatus?,
    override val id: Long? = null
) : Account
