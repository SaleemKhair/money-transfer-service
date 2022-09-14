package org.bsf.transfers

import org.bsf.transfers.domain.AccountModel
import org.bsf.transfers.domain.MoneyTransferModel
import org.bsf.transfers.repositories.AccountRepository
import org.bsf.transfers.repositories.TransferRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class Beans {

    @Bean
    fun accountModelFactory(
        accountRepository: AccountRepository
    ): AccountModel.Factory = AccountModel.Factory(accountRepository)

    @Bean
    fun moneyTransferModelFactory(
        transferRepository: TransferRepository,
        accountRepository: AccountRepository
    ): MoneyTransferModel.Factory = MoneyTransferModel.Factory(transferRepository, accountRepository)

}