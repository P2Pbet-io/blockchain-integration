package com.p2pbet.scheduler.master.properties

import com.p2pbet.persistance.entity.enums.ContractType
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated

@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "logs.master")
data class MasterLogProperties(
    val contracts: Map<ContractType, String>
)