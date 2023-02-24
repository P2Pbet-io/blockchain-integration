package com.p2pbet.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.p2pbet.client.web3.model.AbstractLog
import com.p2pbet.configuration.ActiveMQConfiguration.Companion.AUCTION_QUEUE
import com.p2pbet.configuration.ActiveMQConfiguration.Companion.BINARY_QUEUE
import com.p2pbet.configuration.ActiveMQConfiguration.Companion.CUSTOM_QUEUE
import com.p2pbet.configuration.ActiveMQConfiguration.Companion.JACKPOT_QUEUE
import com.p2pbet.persistance.entity.enums.ContractType
import com.p2pbet.service.model.queue.LogQueueMessageDTO
import mu.KLogger
import mu.KotlinLogging
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service

@Service
class QueueSenderService(
    val jmsTemplate: JmsTemplate,
    val objectMapper: ObjectMapper
) {
    private val logger: KLogger = KotlinLogging.logger { }

    fun sendLog(newLogs: List<AbstractLog>, contractType: ContractType) =
        newLogs
            .asSequence()
            .onEach {
                logger.info { "Sending to core: ${it::class.java.simpleName}. Type: $contractType. Hash: ${it.transactionHash}" }
            }
            .map {
                LogQueueMessageDTO(
                    contractType = contractType,
                    logName = it::class.java.simpleName,
                    payload = it
                )
            }
            .map {
                objectMapper.convertValue(it, Map::class.java)
            }
            .map(objectMapper::writeValueAsString)
            .map {
                when (contractType) {
                    ContractType.AUCTION -> AUCTION_QUEUE
                    ContractType.CUSTOM -> CUSTOM_QUEUE
                    ContractType.BINARY -> BINARY_QUEUE
                    ContractType.JACKPOT -> JACKPOT_QUEUE
                } to it
            }
            .toList()
            .forEach {
                jmsTemplate.send(it.first) { session ->
                    val message = session.createTextMessage()
                    message.text = it.second
                    message
                }
            }
}