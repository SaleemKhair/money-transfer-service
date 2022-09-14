package org.bsf.transfers.repositories

import org.bsf.transfers.common.AccountStatus
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import java.math.BigDecimal
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@DataJpaTest
@DirtiesContext
class AccountRepositoryTest {

    @Autowired
    lateinit var accountRepository: AccountRepository

    @PersistenceContext
    lateinit var entityManager: EntityManager


    @Nested
    inner class Reads {
        @Test
        fun `given IBAN for existing account, when calling findByIban, then account should be returned`() {
            val account = accountRepository.findAccountByIban(existingAccountIban)
            Assertions.assertTrue(account.isPresent)
            Assertions.assertEquals(existingAccountIban, account.get().iban)
            Assertions.assertEquals(BigDecimal("20000.00"), account.get().balance)
            Assertions.assertEquals(AccountStatus.ACTIVE, account.get().status)
        }
    }

    @Nested
    inner class Writes {
        private val amount = BigDecimal("700.00")
        @Test
        fun `given IBAN for existing Account, when calling updateBalanceByUuid, then account balance should be updated`() {
            val optional = accountRepository.findAccountByIban(existingAccountIban)

            accountRepository.updateBalanceByIban(amount, optional.get().iban)

            entityManager.refresh(optional.get())

            val account = accountRepository.findAccountByIban(optional.get().iban)
            Assertions.assertEquals(amount, account.get().balance)
        }
    }

    companion object {
        const val existingAccountIban = "RO49AAAA1B31245007593840000577555"
    }
}