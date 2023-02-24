package com.p2pbet.service.model.custom

import com.p2pbet.persistance.entity.enums.ContractType
import com.p2pbet.service.model.BaseExecutionModel

data class CloseCustomBetDTO(
    val betId: Long,
    val finalValue: String,
    val targetSideWon: Boolean
) : BaseExecutionModel(
    contractType = ContractType.CUSTOM
)