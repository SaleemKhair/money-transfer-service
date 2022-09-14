package org.bsf.transfers.services

import org.bsf.transfers.common.AccountStatus
import org.bsf.transfers.common.TransactionStatus
import org.bsf.transfers.domain.AccountView
import org.bsf.transfers.domain.MoneyTransferView
import org.bsf.transfers.repositories.TransferRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@SpringBootTest
class MoneyTransfersServiceTest {

    @Autowired
    lateinit var moneyTransfersService: MoneyTransfersService

    @Autowired
    lateinit var transferRepository: TransferRepository

    @Test
    fun `when calling transferMoney, on valid request, transfer will be applied`(){
        val sender = AccountView(beneficiaryIban, BigDecimal("10000"), AccountStatus.ACTIVE)
        val beneficiary = AccountView(senderIban, BigDecimal("10000"), AccountStatus.ACTIVE)
        val amount = BigDecimal("10000")
        val uuid = UUID.randomUUID().toString()
        val transferMoney = moneyTransfersService.transferMoney(
            MoneyTransferView(
                beneficiary, sender, amount, LocalDateTime.now(),
                LocalDateTime.now().plusDays(1), TransactionStatus.PENDING, uuid
            )
        )

        moneyTransfersService.transferMoney(transferMoney)
        val transfer = transferRepository.findByUuid(uuid)
        Assertions.assertTrue(transfer.isPresent)
        Assertions.assertTrue(sender.balance.minus(amount).compareTo(transfer.get().sender.balance) == 0)

    }

    companion object {
        const val beneficiaryIban = "RO49AAAA1B31245007593840000577555"
        const val senderIban = "RO49AAAA1B31245007593840000577553"
    }
}