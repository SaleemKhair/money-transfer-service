package org.bsf.transfers.services

import org.bsf.transfers.common.MoneyTransfer
import org.bsf.transfers.domain.AccountModel
import org.bsf.transfers.domain.AccountView
import org.bsf.transfers.domain.MoneyTransferModel
import org.bsf.transfers.domain.MoneyTransferView
import org.springframework.stereotype.Service

@Service
class MoneyTransfersService(
    private val accountModelFactory: AccountModel.Factory,
    private val mTransferModelFactory: MoneyTransferModel.Factory
) {

    fun transferMoney(moneyTransfer: MoneyTransfer): MoneyTransferView {
        val sender = accountModelFactory.fromAccount(moneyTransfer.sender)
        val beneficiary = accountModelFactory.fromAccount(moneyTransfer.beneficiary)

        sender.validate()
        beneficiary.validate()

        val transferModel = mTransferModelFactory.fromMoneyTransfer(moneyTransfer)

        transferModel.validate()

        transferModel.persist()

        transferModel.process()

        beneficiary.credit(moneyTransfer.amount)
        sender.debit(moneyTransfer.amount)

        return toMoneyTransferView(sender, beneficiary, transferModel)
    }

    private fun toMoneyTransferView(
        sender: AccountModel,
        beneficiary: AccountModel,
        transferModel: MoneyTransferModel
    ): MoneyTransferView {
        val senderView = AccountView(sender.iban, sender.balance, sender.status, sender.id)
        val beneficiaryView = AccountView(beneficiary.iban, beneficiary.balance, beneficiary.status, beneficiary.id)
        return MoneyTransferView(
            beneficiary = beneficiaryView,
            sender = senderView,
            amount = transferModel.amount,
            issuedAt = transferModel.issuedAt,
            processedAt = transferModel.processedAt!!,
            status = transferModel.status!!,
            uuid = transferModel.uuid,
            id = transferModel.id
        )
    }
}