package org.bsf.transfers.repositories

import org.bsf.transfers.common.Account
import org.bsf.transfers.common.MoneyTransfer
import org.bsf.transfers.domain.entities.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Repository
@Transactional
interface AccountRepository: JpaRepository<AccountEntity, Long>{

    fun findAccountByIban(iban: String): Optional<AccountEntity>
    fun <T: Account>save(moneyTransfer: T): T

    @Modifying
    @Query("update AccountEntity a set a.balance = ?1 where a.iban = ?2")
    fun updateBalanceByIban(balance: BigDecimal, iban: String): Int

}
