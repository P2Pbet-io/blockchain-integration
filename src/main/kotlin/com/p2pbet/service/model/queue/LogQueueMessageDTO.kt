package com.p2pbet.service.model.queue

import com.p2pbet.client.web3.model.AbstractLog
import com.p2pbet.persistance.entity.enums.ContractType

data class LogQueueMessageDTO(
    val contractType: ContractType,
    val logName: String,
    val payload: AbstractLog
)
