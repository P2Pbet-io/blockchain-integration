package com.p2pbet.client.signification.configuration

import com.p2pbet.client.signification.api.SignificationApi
import com.p2pbet.configuration.feign.FeignClientConfigurationHelper
import com.p2pbet.configuration.feign.InternalServiceInterceptor
import com.p2pbet.client.signification.property.SignificationClientProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SignificationClientProperties::class)
class SignificationFeignConfiguration(
    private val helper: FeignClientConfigurationHelper,
    private val properties: SignificationClientProperties,
    private val interceptor: InternalServiceInterceptor
) {
    @Bean
    fun significationApi(): SignificationApi = helper.buildClient(
        api = SignificationApi::class.java,
        url = properties.url.toString(),
        requestLogLevel = properties.requestLogLevel,
        responseLogLevel = properties.responseLogLevel,
        interceptors = listOf(interceptor)
    )
}
