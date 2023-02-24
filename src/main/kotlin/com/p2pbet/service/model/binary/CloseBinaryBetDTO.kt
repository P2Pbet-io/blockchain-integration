package com.p2pbet.service.model.binary

import com.p2pbet.persistance.entity.enums.ContractType
import com.p2pbet.service.model.BaseExecutionModel

data class CloseBinaryBetDTO(
    val betId: Long,
    val lockedValue: String,
    val finalValue: String,
    val targetSideWon: Boolean
) : BaseExecutionModel(
    contractType = ContractType.BINARY
)