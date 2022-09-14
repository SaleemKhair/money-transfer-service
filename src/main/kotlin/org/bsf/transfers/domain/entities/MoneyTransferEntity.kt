package org.bsf.transfers.domain.entities

import org.bsf.transfers.common.MoneyTransfer
import org.bsf.transfers.common.TransactionStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "Transfers", uniqueConstraints = [
    UniqueConstraint(name = "uc_transfers_uuid", columnNames = ["uuid"])
])
class MoneyTransferEntity(
    @Id
    @SequenceGenerator(name = "transfers_seq", sequenceName = "transfers_seq", allocationSize = 1001)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transfers_seq")
    @Column(name = "id", nullable = false)
    override var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "beneficiary_iban")
    override val beneficiary: AccountEntity,

    @ManyToOne
    @JoinColumn(name = "sender_iban")
    override val sender: AccountEntity,

    @Column(name = "amount", nullable = false)
    override val amount: BigDecimal,

    @Column(name = "issued_date_time", nullable = false)
    override val issuedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "processed_date_time", nullable = true)
    override var processedAt: LocalDateTime?,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    override var status: TransactionStatus?,

    @Column(name = "uuid", nullable = false)
    override val uuid: String

) : MoneyTransfer {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MoneyTransferEntity

        if (id != other.id) return false
        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + uuid.hashCode()
        return result
    }
}