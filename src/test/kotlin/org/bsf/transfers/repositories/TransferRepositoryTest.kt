package org.bsf.transfers.repositories

import org.bsf.transfers.common.TransactionStatus
import org.bsf.transfers.domain.entities.MoneyTransferEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@DataJpaTest
class TransferRepositoryTest {

    @Autowired
    lateinit var transferRepository: TransferRepository

    @Autowired
    lateinit var accountRepository: AccountRepository

    val transferUUID = UUID.randomUUID().toString()

    @BeforeEach
    internal fun setUp() {
        MoneyTransferEntity(
            beneficiary = accountRepository.findAccountByIban(beneficiaryIban).get(),
            sender = accountRepository.findAccountByIban(senderIban).get(),
            status = TransactionStatus.PENDING,
            amount = BigDecimal("2000.00"),
            issuedAt = LocalDateTime.now(),
            processedAt = LocalDateTime.now().plusDays(1),
            uuid = transferUUID
        ).also {
            transferRepository.save(it)
        }
    }

    @Test
    fun `given valid transfer uuid, when calling getByUuid, then corresponding money-transfer should be returned`() {
        val transfer = transferRepository.findByUuid(transferUUID)
        Assertions.assertTrue(transfer.isPresent)
        Assertions.assertEquals(beneficiaryIban, transfer.get().beneficiary.iban)
        Assertions.assertEquals(senderIban, transfer.get().sender.iban)
    }

    companion object {
        const val beneficiaryIban = "RO49AAAA1B31245007593840000577555"
        const val senderIban = "RO49AAAA1B31245007593840000577553"
    }
}