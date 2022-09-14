package org.bsf.transfers.domain

import org.bsf.transfers.common.Account
import org.bsf.transfers.common.AccountStatus
import org.bsf.transfers.common.Persistable
import org.bsf.transfers.common.Validable
import org.bsf.transfers.domain.entities.AccountEntity
import org.bsf.transfers.repositories.AccountRepository
import org.slf4j.LoggerFactory.getLogger
import java.math.BigDecimal

class AccountModel private constructor(
    override val iban: String,
    override val balance: BigDecimal,
    override val status: AccountStatus?,
    override val id: Long?
) : Account, Validable, Persistable<Account> {
    lateinit var accountRepository: AccountRepository

    private constructor(
        accountRepository: AccountRepository,
        account: Account
    ) : this(account.iban, account.balance, account.status, account.id) {
        this.accountRepository = accountRepository
    }

    fun debit(amount: BigDecimal) {
        val optional = accountRepository.findAccountByIban(iban)
        accountRepository.updateBalanceByIban(optional.get().balance.minus(amount), iban)
        info("Debit applied on account with iban $iban")
    }

    fun credit(amount: BigDecimal) {
        val optional = accountRepository.findAccountByIban(iban)
        accountRepository.updateBalanceByIban(optional.get().balance.plus(amount), iban)
        info("Credit applied on account with iban $iban")
    }

    override fun validate() {
        val optional = accountRepository.findAccountByIban(iban)
        if (!optional.isPresent) {
            Companion.error("No record found for account with IBAN $iban")
            throw AccountDoesNotExist("No record found for account with IBAN $iban")
        }
        val accountEntity = optional.get()
        if (AccountStatus.INACTIVE == accountEntity.status) {
            Companion.error("Account with IBAN $iban is INACTIVE")
            throw AccountInactive("Account with IBAN $iban is INACTIVE")
        }
    }

    override fun persist(): Account {
        val optional = accountRepository.findAccountByIban(iban)
        if (optional.isPresent) {
            Companion.error("There is already an account with iban $iban")
            throw AccountAlreadyExists("There is already an account with iban $iban")
        }
        info("Saving Account with iban $iban")
        return accountRepository.save(convertToEntity(this))

    }

    class Factory(private val accountRepository: AccountRepository) {
        fun fromIban(iban: String): AccountModel =
            AccountModel(accountRepository, accountRepository.findAccountByIban(iban).get())

        fun fromAccount(account: Account) =
            AccountModel(accountRepository, account)
    }

    private fun convertToEntity(account: Account): AccountEntity =
        AccountEntity(iban = iban, status = status, balance = balance, id = id)

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger = getLogger(javaClass::class.java)

        fun info(s: String) {
            logger.info(s)
        }

        fun error(s: String, throwable: Throwable? = null) {
            logger.error(s, throwable)
        }

    }
}

class AccountInactive(
    override val message: String,
    override val cause: Throwable? = null
) : IllegalStateException()

class AccountAlreadyExists(
    override val message: String?,
    override val cause: Throwable? = null
) : IllegalStateException()

class AccountDoesNotExist(
    override val message: String?,
    override val cause: Throwable? = null
) : IllegalStateException()

