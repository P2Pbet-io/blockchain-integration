package com.p2pbet.persistance.entity

import com.p2pbet.persistance.entity.enums.ContractType
import com.p2pbet.persistance.entity.enums.ExecutionStatus
import org.hibernate.annotations.Type
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "execution_contract")
@EntityListeners(AuditingEntityListener::class)
class ExecutionContractEntity(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    @Column(name = "log_type")
    val logType: ContractType,

    @Column(name = "nonce")
    var nonce: Long? = null,

    @Column(name = "governance_address")
    var governanceAddress: String? = null,

    @Column(name = "contract_address")
    var contractAddress: String? = null,

    @Column(name = "gas_limit")
    var gasLimit: Long? = null,

    @Column(name = "gas_price")
    var gasPrice: Long? = null,

    @Column(name = "fee")
    var fee: BigDecimal? = null,

    @Type(type = "json")
    @Column(name = "data")
    val data: Map<*, *>,

    @Column(name = "encoded_tx_data")
    val encodedTxData: String,

    @Column(name = "signed_tx_data")
    var signedTxData: String? = null,

    @Column(name = "transaction_hash")
    var transactionHash: String? = null,

    @Column(name = "error_message")
    var errorMessage: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "execution_status")
    var executionStatus: ExecutionStatus = ExecutionStatus.PENDING
) {
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    lateinit var createdDate: LocalDateTime

    @LastModifiedDate
    @Column(name = "modified_date", nullable = false)
    lateinit var modifiedDate: LocalDateTime
}

