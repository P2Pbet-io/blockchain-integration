package com.p2pbet.scheduler.execution

import com.p2pbet.scheduler.execution.dto.ExecutionJobData
import com.p2pbet.scheduler.util.helper.JobExecutionContextHelper.deleteCurrentJob
import com.p2pbet.scheduler.util.helper.JobExecutionContextHelper.getDataObject
import com.p2pbet.service.ExecutionService
import mu.KLogger
import mu.KotlinLogging
import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component

@Component
@DisallowConcurrentExecution
class ExecutionJob(
    val executionService: ExecutionService
) : Job {
    private val logger: KLogger = KotlinLogging.logger { }

    override fun execute(context: JobExecutionContext) {
        val jobData = runCatching { context.getDataObject(ExecutionJobData::class.java) }
            .getOrElse {
                logger.error(it.message, it)
                return
            }

        execute(jobData, context)
    }

    fun execute(jobData: ExecutionJobData, context: JobExecutionContext) {
        runCatching {
            logger.info { "Start execution ${jobData.id}" }
            executionService.scheduleCheckStatusJob(
                execution = executionService.executeTransaction(jobData.id)
            )
            logger.info { "End of execution ${jobData.id}" }
        }.onFailure {
            logger.error("Execution error", it);
            executionService.markFailed(jobData.id, it.message ?: "empty error", true)
        }
        context.deleteCurrentJob()
    }
}