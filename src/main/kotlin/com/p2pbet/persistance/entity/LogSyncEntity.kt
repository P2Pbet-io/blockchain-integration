package com.p2pbet.persistance.entity

import com.p2pbet.persistance.entity.enums.ContractType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "log_sync")
@EntityListeners(AuditingEntityListener::class)
class LogSyncEntity(
    @Id
    @Column(name = "contract_address")
    var contractAddress: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    var name: ContractType,

    @Column(name = "last_block_sync")
    var lastBlockSync: Long,
) {
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    lateinit var createdDate: LocalDateTime

    @LastModifiedDate
    @Column(name = "modified_date", nullable = false)
    lateinit var modifiedDate: LocalDateTime
}