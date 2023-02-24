package com.p2pbet.persistance.entity

import com.p2pbet.persistance.entity.enums.ContractType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "governance_wallet")
@EntityListeners(AuditingEntityListener::class)
class GovernanceWalletEntity(
    @Id
    @Column(name = "address")
    val address: String,

    @Column(name = "nonce")
    var nonce: Long = 0,

    @Column(name = "balance")
    var balance: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    var type: ContractType,

    @Column(name = "control_contract")
    val controlContract: String
) {
    @CreatedDate
    @Column(name = "created_date")
    lateinit var createdDate: LocalDateTime

    @LastModifiedDate
    @Column(name = "modified_date")
    lateinit var modifiedDate: LocalDateTime
}