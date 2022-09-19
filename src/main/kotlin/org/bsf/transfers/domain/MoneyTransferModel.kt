package org.bsf.transfers.domain

import org.bsf.transfers.common.*
import org.bsf.transfers.repositories.AccountRepository
import org.bsf.transfers.repositories.TransferRepository
import org.bsf.transfers.domain.entities.MoneyTransferEntity
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDateTime

class MoneyTransferModel private constructor(
    override val uuid: String,
    override var beneficiary: Account,
    override var sender: Account,
    override val amount: BigDecimal,
    override val issuedAt: LocalDateTime,
    override val processedAt: LocalDateTime?,
    override val status: TransactionStatus?,
    override var id: Long?
) : MoneyTransfer, Validable, Persistable<MoneyTransfer> {

    private lateinit var transferRepository: TransferRepository
    private lateinit var accountRepository: AccountRepository

    private constructor(
        transferRepository: TransferRepository,
        accountRepository: AccountRepository,
        moneyTransfer: MoneyTransfer
    ) : this(
        moneyTransfer.uuid,
        moneyTransfer.beneficiary,
        moneyTransfer.sender,
        moneyTransfer.amount,
        moneyTransfer.issuedAt,
        moneyTransfer.processedAt,
        moneyTransfer.status,
        moneyTransfer.id
    ) {
        this.transferRepository = transferRepository
        this.accountRepository = accountRepository
        val saved = transferRepository.save(toMoneyTransferEntity())
        this.sender = accountRepository.findAccountByIban(sender.iban).get()
        this.beneficiary = accountRepository.findAccountByIban(beneficiary.iban).get()
        this.id = saved.id
    }

    fun process() {
        val optional = transferRepository.findByUuid(uuid)
        val transactionEntity = optional.get()
        transactionEntity.status = TransactionStatus.PROCESSED
        transactionEntity.processedAt = LocalDateTime.now()
        info("Transfer of uuid $uuid updated status to ${transactionEntity.status}")
        transferRepository.save(transactionEntity)
    }

    override fun validate() {
        if (sender.balance < amount) {
            Companion.error("Sender with iban `${sender.iban}` has insufficient balance. balance: ${sender.balance}, amount: $amount")
            throw InvalidTransfer("Sender has insufficient balance")
        }
    }

    override fun persist(): MoneyTransfer {
        info("Saving transfer with uuid $uuid")
        return transferRepository.save(toMoneyTransferEntity())
    }

    private fun toMoneyTransferEntity(): MoneyTransferEntity  =
        MoneyTransferEntity(
            id = id,
            uuid = uuid,
            issuedAt = issuedAt,
            processedAt = processedAt,
            amount = amount,
            status = status,
            sender = accountRepository.findAccountByIban(sender.iban).get(),
            beneficiary = accountRepository.findAccountByIban(beneficiary.iban).get()
        )

    class Factory(
        private val transferRepository: TransferRepository,
        private val accountRepository: AccountRepository
    ) {
        fun fromMoneyTransfer(moneyTransfer: MoneyTransfer): MoneyTransferModel =
            MoneyTransferModel(
                transferRepository = this.transferRepository,
                accountRepository = this.accountRepository,
                moneyTransfer = moneyTransfer
            )
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

class InvalidTransfer(
    override val message: String?,
    override val cause: Throwable? = null
) : IllegalStateException()


