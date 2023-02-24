package com.p2pbet.service

import com.p2pbet.client.signification.api.SignificationApi
import com.p2pbet.client.signification.api.model.SignEthereumTransactionRequestDTO
import com.p2pbet.client.web3.Web3BSCClient
import com.p2pbet.persistance.entity.ExecutionContractEntity
import com.p2pbet.persistance.entity.enums.ExecutionStatus
import com.p2pbet.persistance.repository.ExecutionContractRepository
import com.p2pbet.scheduler.execution.CheckStatusExecutionJob
import com.p2pbet.scheduler.execution.ExecutionJob
import com.p2pbet.scheduler.execution.configuration.ExecutionConfiguration.Companion.EXECUTION_DURATION_TIME_MINUTES
import com.p2pbet.scheduler.execution.configuration.ExecutionConfiguration.Companion.EXECUTION_REPEAT_INTERVAL
import com.p2pbet.scheduler.execution.configuration.ExecutionConfiguration.Companion.EXECUTION_START_DELAY
import com.p2pbet.scheduler.execution.dto.ExecutionJobData
import com.p2pbet.scheduler.master.properties.MasterLogProperties
import com.p2pbet.scheduler.util.service.SchedulerService
import com.p2pbet.scheduler.util.service.TriggerProperties
import com.p2pbet.service.LogEnrichmentService.Companion.DELAY_CONFIRMATION_BLOCK
import mu.KLogger
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.web3j.utils.Convert
import org.web3j.utils.Convert.fromWei
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*

@Service
class ExecutionService(
    val web3BSCClient: Web3BSCClient,
    val executionContractRepository: ExecutionContractRepository,
    val significationApi: SignificationApi,
    val governanceWalletService: GovernanceWalletService,
    val masterLogProperties: MasterLogProperties,
    val schedulerService: SchedulerService,
) {
    private val logger: KLogger = KotlinLogging.logger { }

    companion object {
        const val GAS_LIMIT = 1000000L
    }

    fun getTransaction(id: UUID) = executionContractRepository.runCatching {
        getById(id)
    }.getOrElse {
        throw RuntimeException("Cannot find execution by id: $id")
    }

    @Transactional
    fun executeTransaction(id: UUID): ExecutionContractEntity {
        var execution = getTransaction(id)
        val governanceWallet = governanceWalletService.getGovernanceWallet(execution.logType)
        val gasPrice = web3BSCClient.getGasPrice()

        if (execution.executionStatus != ExecutionStatus.PENDING) {
            throw RuntimeException("Wrong execution status(id: $id) - ${execution.executionStatus}")
        }

        // Enrichment with blockchain data
        execution.gasPrice = gasPrice.toLong()
        execution.gasLimit = GAS_LIMIT
        execution.governanceAddress = governanceWallet.address
        execution.contractAddress = masterLogProperties.contracts[execution.logType]
        execution.nonce = governanceWallet.nonce
        execution.signedTxData = signTransaction(execution)

        governanceWalletService.incrementAndSave(governanceWallet)

        //Send row transaction
        val txHash = web3BSCClient.sendTransaction(execution.signedTxData!!)
        execution.transactionHash = txHash

        execution.executionStatus = ExecutionStatus.SENT

        // Update data
        execution = executionContractRepository.save(execution)


        logger.info { "Successfully sent transaction with id ${execution.id}. Data sent: ${execution.data}." }

        return execution
    }

    @Transactional
    fun markFailed(id: UUID, message: String, changeStatus: Boolean) {
        val execution = getTransaction(id)
        execution.errorMessage = message
        if (changeStatus) {
            execution.executionStatus = ExecutionStatus.FAILED
        }
        executionContractRepository.save(execution)
    }

    fun checkStatusOfExecution(id: UUID): ExecutionContractEntity {
        val execution = getTransaction(id)

        kotlin.runCatching {
            val transactionReceipt = web3BSCClient.getTransactionReceipt(execution.transactionHash!!)

            if (web3BSCClient.getLastBlock() - transactionReceipt.blockNumber < DELAY_CONFIRMATION_BLOCK.toBigInteger()) {
                execution.modifiedDate = LocalDateTime.now()
                return executionContractRepository.save(execution)
            }


            if (transactionReceipt.status == "0x1") {
                execution.executionStatus = ExecutionStatus.CONFIRMED
            } else {
                execution.errorMessage = transactionReceipt.revertReason
                execution.executionStatus = ExecutionStatus.FAILED
            }

            execution.gasLimit = transactionReceipt.gasUsed.toLong()
            execution.fee = fromWei((execution.gasLimit!! * execution.gasPrice!!).toBigDecimal(), Convert.Unit.ETHER)
        }.onFailure {
            execution.modifiedDate = LocalDateTime.now()
        }
        return executionContractRepository.save(execution)
    }

    private fun signTransaction(execution: ExecutionContractEntity) =
        significationApi.signEthereumTransaction(
            request = SignEthereumTransactionRequestDTO(
                from = execution.governanceAddress!!,
                to = execution.contractAddress!!,
                amount = BigInteger.ZERO,
                data = execution.encodedTxData,
                nonce = execution.nonce!!.toBigInteger(),
                gasLimit = execution.gasLimit!!.toBigInteger(),
                gasPrice = execution.gasPrice!!.toBigInteger()

            )
        ).hex

    fun scheduleExecutionJob(execution: ExecutionContractEntity) =
        schedulerService.schedule(
            jobClass = ExecutionJob::class.java,
            name = "${ExecutionJob::class.java.simpleName}_${execution.id}",
            group = ExecutionJob::class.java.simpleName,
            data = ExecutionJobData(execution.id),
            triggerProperties = TriggerProperties(
                repeatCount = -1,
                repeatIntervalInSeconds = EXECUTION_REPEAT_INTERVAL,
                priority = 15,
                startAt = LocalDateTime.now().plusSeconds(EXECUTION_START_DELAY),
                endAt = LocalDateTime.now().plusMinutes(EXECUTION_DURATION_TIME_MINUTES)
            )
        )

    fun scheduleCheckStatusJob(execution: ExecutionContractEntity) =
        schedulerService.schedule(
            jobClass = CheckStatusExecutionJob::class.java,
            name = "${CheckStatusExecutionJob::class.java.simpleName}_${execution.id}",
            group = CheckStatusExecutionJob::class.java.simpleName,
            data = ExecutionJobData(execution.id),
            triggerProperties = TriggerProperties(
                repeatCount = -1,
                repeatIntervalInSeconds = EXECUTION_REPEAT_INTERVAL,
                priority = 15,
                startAt = LocalDateTime.now().plusSeconds(EXECUTION_START_DELAY),
                endAt = LocalDateTime.now().plusMinutes(EXECUTION_DURATION_TIME_MINUTES)
            )
        )
}