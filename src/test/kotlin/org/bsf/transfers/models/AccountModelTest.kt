package org.bsf.transfers.models


import org.bsf.transfers.common.AccountStatus
import org.bsf.transfers.domain.*
import org.bsf.transfers.repositories.AccountRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import java.math.BigDecimal

@SpringBootTest
@DirtiesContext
class AccountModelTest {

    @Autowired
    lateinit var accountModelFactory: AccountModel.Factory

    @Autowired
    lateinit var accountRepository: AccountRepository


    @Test
    fun `given IBAN for valid account, when calling credit, account should be credited with amount`(){

        var optional = accountRepository.findAccountByIban(validAccountIban)
        val accountModel = accountModelFactory.fromAccount(optional.get())
        val balance = optional.get().balance
        val amount = BigDecimal("2000")

        accountModel.credit(amount)

        optional = accountRepository.findAccountByIban(validAccountIban)
        Assertions.assertEquals(balance.plus(amount), optional.get().balance)
    }

    @Test
    fun `given IBAN for valid account, when calling debit, account should be debited with amount`() {
        var optional = accountRepository.findAccountByIban(validAccountIban)
        val accountModel = accountModelFactory.fromAccount(optional.get())
        val balance = optional.get().balance
        val amount = BigDecimal("2000")
        accountModel.debit(amount)

        optional = accountRepository.findAccountByIban(validAccountIban)
        Assertions.assertEquals(balance.minus(amount), optional.get().balance)
    }

    @Nested
    inner class CallingPersist {
        @Test
        fun `given account that already exists, then should throw AccountAlreadyExists exception`() {
            Assertions.assertThrows(AccountAlreadyExists::class.java) {
                val optional = accountRepository.findAccountByIban(validAccountIban)
                val accountModel = accountModelFactory.fromAccount(optional.get())
                accountModel.persist()
            }
        }
        @Test
        fun `given valid account and account doesn't exist, then should persist to DB`() {
            val accountView = AccountView(nonExistingAccountIban, BigDecimal("30000.00"), AccountStatus.ACTIVE)
            val accountModel = accountModelFactory.fromAccount(accountView)

            accountModel.persist()

            val optional = accountRepository.findAccountByIban(nonExistingAccountIban)
            Assertions.assertTrue(optional.isPresent)
        }
    }

    @Nested
    inner class CallingValidate {

        @Test
        fun `given inactive account, then should throw AccountInactive exception`() {
            Assertions.assertThrows(AccountInactive::class.java) {
                val optional = accountRepository.findAccountByIban(inactiveAccountIban)
                val accountModel = accountModelFactory.fromAccount(optional.get())
                accountModel.validate()
            }
        }

        @Test
        fun `given non existing account, then should throw AccountDoesNotExist exception`() {
            Assertions.assertThrows(AccountDoesNotExist::class.java) {
                val accountView = AccountView(nonExistingAccountIban, BigDecimal("1000.00"), AccountStatus.ACTIVE)
                val accountModel = accountModelFactory.fromAccount(accountView)
                accountModel.validate()
            }
        }
    }
    companion object {
        const val validAccountIban = "RO49AAAA1B31245007593840000577555"
        const val nonExistingAccountIban = "RO49AAAA1B31245007593840000577557"
        const val inactiveAccountIban = "RO49AAAA1B31245007593840000577559"
    }
}