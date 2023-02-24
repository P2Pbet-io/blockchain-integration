package com.p2pbet.scheduler.master.dto

import com.p2pbet.persistance.entity.enums.ContractType

data class MasterJobData(
    val contractType: ContractType,
    val address: String
)
