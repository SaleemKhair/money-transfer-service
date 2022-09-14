package org.bsf.transfers.repositories

import org.bsf.transfers.common.MoneyTransfer
import org.bsf.transfers.domain.entities.MoneyTransferEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional
interface TransferRepository : JpaRepository<MoneyTransferEntity, Long> {

    fun findByUuid(uuid: String): Optional<MoneyTransferEntity>
    fun findAllByUuid(uuid: String): List<MoneyTransferEntity>
    fun <T: MoneyTransfer>save(moneyTransfer: T): T
}
