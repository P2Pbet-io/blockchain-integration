package com.p2pbet.service.model.jackpot

import com.p2pbet.persistance.entity.enums.ContractType
import com.p2pbet.service.model.BaseExecutionModel
import java.math.BigDecimal
import java.time.OffsetDateTime

class CreateJackpotBetDTO(
    val eventId: String,
    val lockTime: OffsetDateTime,
    val expirationTime: OffsetDateTime,
    val requestAmount: BigDecimal
) : BaseExecutionModel(
    contractType = ContractType.JACKPOT
)