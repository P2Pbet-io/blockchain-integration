package com.p2pbet.configuration

import org.apache.activemq.command.ActiveMQQueue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.jms.Queue


@Configuration
class ActiveMQConfiguration {
    companion object {
        const val AUCTION_QUEUE = "bsc.contract.auction"
        const val BINARY_QUEUE = "bsc.contract.binary"
        const val CUSTOM_QUEUE = "bsc.contract.custom"
        const val JACKPOT_QUEUE = "bsc.contract.jackpot"
    }

    @Bean
    fun auctionQueue(): Queue = ActiveMQQueue(AUCTION_QUEUE)

    @Bean
    fun binaryQueue(): Queue = ActiveMQQueue(BINARY_QUEUE)

    @Bean
    fun customQueue(): Queue = ActiveMQQueue(CUSTOM_QUEUE)

    @Bean
    fun jackpotQueue(): Queue = ActiveMQQueue(JACKPOT_QUEUE)
}