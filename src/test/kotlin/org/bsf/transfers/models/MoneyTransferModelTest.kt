package org.bsf.transfers.models

import org.bsf.transfers.common.TransactionStatus
import org.bsf.transfers.domain.AccountView
import org.bsf.transfers.domain.InvalidTransfer
import org.bsf.transfers.domain.MoneyTransferModel
import org.bsf.transfers.domain.MoneyTransferView
import org.bsf.transfers.repositories.AccountRepository
import org.bsf.transfers.repositories.TransferRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@SpringBootTest
class MoneyTransferModelTest {

    @Autowired
    lateinit var transferRepository: TransferRepository

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var moneyTransferModelFactory: MoneyTransferModel.Factory

    @Test
    fun `given transfer valid accounts for sender and beneficiary, when calling persist, then should be saved`() {
        val beneficiary = accountRepository.findAccountByIban(beneficiaryIban).get()
        val sender = accountRepository.findAccountByIban(beneficiaryIban).get()
        val amount = BigDecimal("7000.00")
        val uuid = UUID.randomUUID().toString()
        MoneyTransferView(
            beneficiary = AccountView(beneficiaryIban, beneficiary.balance, beneficiary.status),
            sender = AccountView(sender.iban, sender.balance, sender.status),
            amount = amount,
            issuedAt = LocalDateTime.now(),
            processedAt = LocalDateTime.now().plusDays(1),
            status = TransactionStatus.PENDING,
            uuid = uuid,
        ).also {
            val transferModel = moneyTransferModelFactory.fromMoneyTransfer(it)
            transferModel.persist()
        }

        val optional = transferRepository.findByUuid(uuid)
        Assertions.assertTrue(optional.isPresent)
    }

    @Nested
    inner class CallingValidate {
        @Test
        fun `given valid accounts, but sender don't have enough balance, then should throw InvalidTransfer exception`() {
            val beneficiary = accountRepository.findAccountByIban(beneficiaryIban).get()
            val sender = accountRepository.findAccountByIban(senderWithLittleBalance).get()
            val amount = BigDecimal("7000.00")
            val uuid = UUID.randomUUID().toString()
            MoneyTransferView(
                beneficiary = AccountView(beneficiaryIban, beneficiary.balance, beneficiary.status),
                sender = AccountView(sender.iban, sender.balance, sender.status),
                amount = amount,
                issuedAt = LocalDateTime.now(),
                processedAt = LocalDateTime.now().plusDays(1),
                status = TransactionStatus.PENDING,
                uuid = uuid
            ).also {
                val transferModel = moneyTransferModelFactory.fromMoneyTransfer(it)
                transferModel.persist()
                val exception = Assertions.assertThrows(InvalidTransfer::class.java) {
                    transferModel.validate()
                }
                Assertions.assertEquals("Sender has insufficient balance",exception.message)
            }
        }
    }


    companion object {
        const val beneficiaryIban = "RO49AAAA1B31245007593840000577555"
        const val senderIban = "RO49AAAA1B31245007593840000577553"
        const val senderWithLittleBalance = "RO49AAAA1B31245007593840000577533"
    }
}