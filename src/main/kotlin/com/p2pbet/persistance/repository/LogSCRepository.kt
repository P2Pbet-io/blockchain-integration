package com.p2pbet.persistance.repository

import com.p2pbet.persistance.entity.LogSCEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LogSCRepository : JpaRepository<LogSCEntity, UUID> {
    fun existsBySyncHash(syncHash: String): Boolean
}