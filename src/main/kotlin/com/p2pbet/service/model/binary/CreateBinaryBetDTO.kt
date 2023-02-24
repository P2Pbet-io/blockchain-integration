package com.p2pbet.service.model.binary

import com.p2pbet.persistance.entity.enums.ContractType
import com.p2pbet.service.model.BaseExecutionModel
import java.time.OffsetDateTime

class CreateBinaryBetDTO(
    val eventId: String,
    val lockTime: OffsetDateTime,
    val expirationTime: OffsetDateTime
) : BaseExecutionModel(
    contractType = ContractType.BINARY
)