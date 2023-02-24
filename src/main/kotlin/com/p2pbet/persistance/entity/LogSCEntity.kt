package com.p2pbet.persistance.entity

import com.p2pbet.client.web3.model.LogEnumMapper
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "log_sc")
@TypeDef(name = "json", typeClass = JsonBinaryType::class)
@EntityListeners(AuditingEntityListener::class)
class LogSCEntity(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(name = "contract_address")
    val contractAddress: String,

    @Column(name = "block_number")
    val blockNumber: Long,

    @Column(name = "transaction_hash")
    val transactionHash: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "log_type")
    val logType: LogEnumMapper,

    @Column(name = "sync_hash")
    val syncHash: String,

    @Type(type = "json")
    @Column(name = "data")
    val data: Map<*, *>
) {
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    lateinit var createdDate: LocalDateTime
}
