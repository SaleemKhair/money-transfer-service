package org.bsf.transfers.services

import org.bsf.transfers.common.Account
import org.bsf.transfers.domain.AccountModel
import org.bsf.transfers.domain.AccountView
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountModelFactory: AccountModel.Factory
) {
    fun inquireBy(iban:String): AccountView = toAccountView(accountModelFactory.fromIban(iban))

    fun create(account: Account): Account = toAccountView(accountModelFactory.fromAccount(account).persist())


    private fun toAccountView(account: Account): AccountView {
        return AccountView(account.iban, account.balance, account.status, account.id)
    }


}