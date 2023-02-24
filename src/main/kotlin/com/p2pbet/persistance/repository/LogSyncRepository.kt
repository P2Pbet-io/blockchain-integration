package com.p2pbet.persistance.repository

import com.p2pbet.persistance.entity.LogSyncEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LogSyncRepository : JpaRepository<LogSyncEntity, String>