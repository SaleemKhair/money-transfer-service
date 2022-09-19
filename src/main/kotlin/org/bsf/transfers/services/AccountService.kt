package org.bsf.transfers.services

import org.bsf.transfers.common.Account
import org.bsf.transfers.domain.AccountModel
import org.bsf.transfers.domain.AccountView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountModelFactory: AccountModel.Factory
) {
    @Transactional
    fun inquireBy(iban:String): AccountView = toAccountView(accountModelFactory.fromIban(iban))
    @Transactional
    fun create(account: Account): Account = toAccountView(accountModelFactory.fromAccount(account).persist())


    private fun toAccountView(account: Account): AccountView {
        return AccountView(account.iban, account.balance, account.status, account.id)
    }


}