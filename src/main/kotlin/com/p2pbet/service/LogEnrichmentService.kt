package com.p2pbet.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.p2pbet.client.web3.Web3BSCClient
import com.p2pbet.persistance.entity.LogSCEntity
import com.p2pbet.persistance.entity.LogSyncEntity
import com.p2pbet.persistance.entity.enums.ContractType
import com.p2pbet.persistance.repository.LogSCRepository
import com.p2pbet.persistance.repository.LogSyncRepository
import mu.KLogger
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.min

@Service
class LogEnrichmentService(
    val web3BSCClient: Web3BSCClient,
    val logSyncRepository: LogSyncRepository,
    val logSCRepository: LogSCRepository,
    val objectMapper: ObjectMapper,
    val governanceWalletService: GovernanceWalletService,
    val queueSenderService: QueueSenderService
) {
    companion object {
        const val DELAY_CONFIRMATION_BLOCK = 2
    }

    private val logger: KLogger = KotlinLogging.logger { }

    @Transactional
    fun enrichLogsFromContract(address: String, contractType: ContractType) {
        val currentBlockNumber = web3BSCClient.getLastBlock().toLong()
        // 2 - sync delay from current
        val blockNumber = currentBlockNumber - DELAY_CONFIRMATION_BLOCK
        val contractSync = getOrCreateSync(address, contractType, blockNumber)

        // Max batch size - 50
        val newBlockToSync = min(blockNumber, contractSync.lastBlockSync + 50)

        if (contractSync.lastBlockSync >= newBlockToSync) {
            logger.info { "Nothing to enrich $contractType. Sync block number - ${contractSync.lastBlockSync}. Current block number - $blockNumber" }
            return
        }

        val newLogs = fetchLogAndSave(
            address = address,
            fromBlock = contractSync.lastBlockSync + 1,
            toBlock = newBlockToSync
        )

        // refresh balance
        governanceWalletService.refreshBalance(contractType, blockNumber)

        logger.info { "End of sync $contractType. From ${contractSync.lastBlockSync + 1} to $newBlockToSync. Caught ${newLogs.size} new logs." }

        // Send to core
        queueSenderService.sendLog(
            newLogs = newLogs,
            contractType = contractType
        )

        // Update sync info
        contractSync.lastBlockSync = newBlockToSync
        logSyncRepository.save(contractSync)
    }

    fun fetchLogAndSave(address: String, fromBlock: Long, toBlock: Long) =
        web3BSCClient.getLogs(
            address = address,
            fromBlock = fromBlock,
            toBlock = toBlock
        )
            .map {
                with(objectMapper.convertValue(it, Map::class.java)) {
                    LogSCEntity(
                        contractAddress = it.contractAddress,
                        blockNumber = it.blockNumber.toLong(),
                        transactionHash = it.transactionHash,
                        logType = it.logType,
                        syncHash = "${
                            (it.transactionHash + objectMapper.writeValueAsString(this)).hashCode()
                        }_${it.transactionHash}",
                        data = this
                    ) to it
                }
            }
            .filterNot {
                logSCRepository.existsBySyncHash(it.first.syncHash)
            }
            .map {
                logSCRepository.save(it.first)
                it.second
            }


    private fun getOrCreateSync(address: String, contractType: ContractType, currentBlock: Long) =
        if (logSyncRepository.existsById(address)) {
            logSyncRepository.getById(address)
        } else {
            logSyncRepository.save(
                LogSyncEntity(
                    contractAddress = address,
                    name = contractType,
                    lastBlockSync = currentBlock
                )
            )
        }
}