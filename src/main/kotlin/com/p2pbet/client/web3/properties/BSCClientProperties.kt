package com.p2pbet.client.web3.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import java.net.URL
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "external-service.bsc")
data class BSCClientProperties(
    @field: NotNull
    val url: URL,
    @field: NotEmpty
    val apiKey: String
)
