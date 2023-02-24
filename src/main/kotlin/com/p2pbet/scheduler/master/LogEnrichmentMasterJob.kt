package com.p2pbet.scheduler.master

import com.p2pbet.scheduler.master.dto.MasterJobData
import com.p2pbet.scheduler.util.helper.JobExecutionContextHelper.getDataObject
import com.p2pbet.service.LogEnrichmentService
import mu.KLogger
import mu.KotlinLogging
import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component

@Component
@DisallowConcurrentExecution
class LogEnrichmentMasterJob(
    val logEnrichmentService: LogEnrichmentService
) : Job {
    private val logger: KLogger = KotlinLogging.logger { }

    override fun execute(context: JobExecutionContext) {
        val jobData = runCatching { context.getDataObject(MasterJobData::class.java) }
            .getOrElse {
                logger.error(it.message, it)
                return
            }

        logger.info { "Start enrichment ${jobData.contractType} - ${jobData.address}" }

        logEnrichmentService.enrichLogsFromContract(
            address = jobData.address,
            contractType = jobData.contractType
        )

        logger.info { "End of enrichment ${jobData.contractType} - ${jobData.address}" }
    }
}