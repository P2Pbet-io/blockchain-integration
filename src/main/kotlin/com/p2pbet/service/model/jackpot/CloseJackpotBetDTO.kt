package com.p2pbet.service.model.jackpot

import com.p2pbet.persistance.entity.enums.ContractType
import com.p2pbet.service.model.BaseExecutionModel

data class CloseJackpotBetDTO(
    val betId: Long,
    val finalValue: String
) : BaseExecutionModel(
    contractType = ContractType.JACKPOT
)