package com.p2pbet.service.model

import com.p2pbet.persistance.entity.enums.ContractType

abstract class BaseExecutionModel(
    val contractType: ContractType
)