package com.p2pbet.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.p2pbet.persistance.entity.ExecutionContractEntity
import com.p2pbet.persistance.entity.enums.ExecutionStatus
import com.p2pbet.persistance.repository.ExecutionContractRepository
import com.p2pbet.scheduler.execution.ExecutionJob
import com.p2pbet.scheduler.execution.configuration.ExecutionConfiguration
import com.p2pbet.scheduler.execution.dto.ExecutionJobData
import com.p2pbet.scheduler.util.service.SchedulerService
import com.p2pbet.scheduler.util.service.TriggerProperties
import com.p2pbet.service.async.AsyncOperationMapper.prepareEncodedTxData
import com.p2pbet.service.model.BaseExecutionModel
import com.p2pbet.service.model.ExecutionResponseDTO
import mu.KLogger
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class AsyncExecutionService(
    val executionContractRepository: ExecutionContractRepository,
    val schedulerService: SchedulerService,
    val objectMapper: ObjectMapper
) {
    private val logger: KLogger = KotlinLogging.logger { }

    fun getExecution(id: UUID) = executionContractRepository.findById(id)
        .orElseGet {
            throw RuntimeException("Execution not found by id: $id")
        }
        .convertToResponse();


    fun <T> startAsyncExecution(request: T): ExecutionResponseDTO where T : BaseExecutionModel =
        executionContractRepository
            .save(
                ExecutionContractEntity(
                    logType = request.contractType,
                    data = objectMapper.convertValue(request, Map::class.java),
                    encodedTxData = request.prepareEncodedTxData(),
                    executionStatus = ExecutionStatus.PENDING
                )
            )
            .scheduleExecutionJob()
            .apply {
                logger.info { "Scheduled ${request::class.java} with execution id: $id" }
            }
            .convertToResponse()


    private fun ExecutionContractEntity.scheduleExecutionJob(): ExecutionContractEntity = apply {
        schedulerService.schedule(
            jobClass = ExecutionJob::class.java,
            name = "${ExecutionJob::class.java.simpleName}_$id",
            group = ExecutionJob::class.java.simpleName,
            data = ExecutionJobData(id),
            triggerProperties = TriggerProperties(
                repeatCount = -1,
                repeatIntervalInSeconds = ExecutionConfiguration.EXECUTION_REPEAT_INTERVAL,
                priority = 15,
                startAt = LocalDateTime.now().plusSeconds(ExecutionConfiguration.EXECUTION_START_DELAY),
                endAt = LocalDateTime.now().plusMinutes(ExecutionConfiguration.EXECUTION_DURATION_TIME_MINUTES)
            )
        )
    }

    private fun ExecutionContractEntity.convertToResponse() = ExecutionResponseDTO(
        id = id,
        contractType = logType,
        executionStatus = executionStatus,
        errorMessage = errorMessage,
        transactionHash = transactionHash
    )
}