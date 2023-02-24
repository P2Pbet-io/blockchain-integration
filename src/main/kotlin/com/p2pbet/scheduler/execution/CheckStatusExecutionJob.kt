package com.p2pbet.scheduler.execution

import com.p2pbet.persistance.entity.enums.ExecutionStatus
import com.p2pbet.scheduler.execution.dto.ExecutionJobData
import com.p2pbet.scheduler.util.helper.JobExecutionContextHelper.deleteCurrentJob
import com.p2pbet.scheduler.util.helper.JobExecutionContextHelper.getDataObject
import com.p2pbet.scheduler.util.helper.JobExecutionContextHelper.isLastTimeTriggerFired
import com.p2pbet.service.ExecutionService
import mu.KLogger
import mu.KotlinLogging
import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component

@Component
@DisallowConcurrentExecution
class CheckStatusExecutionJob(
    val executionService: ExecutionService
) : Job {
    private val logger: KLogger = KotlinLogging.logger { }

    override fun execute(context: JobExecutionContext) {
        val jobData = runCatching { context.getDataObject(ExecutionJobData::class.java) }
            .getOrElse {
                logger.error(it.message, it)
                return
            }

        executeCheckStatus(jobData, context)
    }

    fun executeCheckStatus(jobData: ExecutionJobData, context: JobExecutionContext) {
        runCatching {
            logger.info { "Start execution ${jobData.id}" }
            val execution = executionService.checkStatusOfExecution(jobData.id)
            when (execution.executionStatus) {
                ExecutionStatus.SENT -> {
                    logger.info { "Status of execution ${jobData.id} didn't change" }
                    if (context.isLastTimeTriggerFired()) {
                        logger.error { "Status of execution ${jobData.id}: timeout" }
                        executionService.markFailed(jobData.id, "timeout", true)
                        context.deleteCurrentJob()
                    }
                }
                ExecutionStatus.CONFIRMED -> {
                    logger.info { "Status of execution ${jobData.id} changed to CONFIRMED" }
                    context.deleteCurrentJob()
                }
                ExecutionStatus.FAILED -> {
                    logger.error { "Status of execution ${jobData.id} changed to FAILED" }
                    context.deleteCurrentJob()
                }
                else -> {
                    throw RuntimeException("Error status of execution check status: ${execution.executionStatus}")
                }
            }
        }.onFailure {
            logger.error("Execution error", it);
            executionService.markFailed(jobData.id, it.message ?: "empty error", context.isLastTimeTriggerFired())
            if (context.isLastTimeTriggerFired()) {
                context.deleteCurrentJob()
            }
        }
    }
}