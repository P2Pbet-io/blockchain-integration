package com.p2pbet.client.web3.properties

import com.p2pbet.client.web3.model.LogEnumMapper
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated

@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "logs")
data class LogProperties (
    val eventMapping: Map<LogEnumMapper, String>
)