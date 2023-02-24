package com.p2pbet.persistance.repository

import com.p2pbet.persistance.entity.ExecutionContractEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ExecutionContractRepository : JpaRepository<ExecutionContractEntity, UUID> {

}