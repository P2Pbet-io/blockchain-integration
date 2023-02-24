package com.p2pbet.client.web3.configuration

import com.p2pbet.client.web3.properties.BSCClientProperties
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import java.util.concurrent.TimeUnit

@Configuration
@EnableConfigurationProperties(BSCClientProperties::class)
class BSCWeb3ClientConfiguration(
    private val clientProperties: BSCClientProperties
) {

    @Bean
    fun bscWeb3Client(): Web3j =
        Web3j.build(
            HttpService(
                clientProperties.url.toString(),
                createOkHttpClient()
            )
        )

    fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BASIC }
            )
            .addInterceptor { chain ->
                chain.request().newBuilder()
                    .build()
                    .let(chain::proceed)
            }
            .build()
    }
}