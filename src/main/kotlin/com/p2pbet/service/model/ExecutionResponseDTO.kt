package com.p2pbet.service.model

import com.p2pbet.persistance.entity.enums.ContractType
import com.p2pbet.persistance.entity.enums.ExecutionStatus
import java.util.*

data class ExecutionResponseDTO(
    val id: UUID,
    val contractType: ContractType,
    val executionStatus: ExecutionStatus,
    val transactionHash: String?,
    val errorMessage: String?
)
