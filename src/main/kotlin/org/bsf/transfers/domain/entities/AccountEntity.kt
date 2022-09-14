package org.bsf.transfers.domain.entities

import org.bsf.transfers.common.Account
import org.bsf.transfers.common.AccountStatus
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "Accounts")
data class AccountEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    override var id: Long? = null,

    @Column(name = "balance", nullable = false)
    override var balance: BigDecimal = BigDecimal(0),

    @Column(name = "iban", nullable = false)
    override var iban: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    override var status: AccountStatus? = AccountStatus.INACTIVE,
    @OneToMany(mappedBy = "sender")
    var sentTransfers: List<MoneyTransferEntity>? = null,

    @OneToMany(mappedBy = "beneficiary")
    var receivedTransfers: List<MoneyTransferEntity>? = null

) : Account {

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AccountEntity

        if (id != other.id) return false
        if (iban != other.iban) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + iban.hashCode()
        return result
    }
}

