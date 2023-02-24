package com.p2pbet.scheduler.master.configuration

import com.p2pbet.scheduler.master.LogEnrichmentMasterJob
import com.p2pbet.scheduler.master.dto.MasterJobData
import com.p2pbet.scheduler.master.properties.MasterLogProperties
import com.p2pbet.scheduler.util.service.SchedulerService
import com.p2pbet.scheduler.util.service.TriggerProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import javax.annotation.PostConstruct

@Configuration
@EnableConfigurationProperties(MasterLogProperties::class)
class MasterLogConfiguration(
    val schedulerService: SchedulerService,
    val masterLogProperties: MasterLogProperties
) {
    companion object {
        const val REPEAT_INTERVAL = 15;
        const val START_DELAY = 3;
    }

    @PostConstruct
    fun init() {

        masterLogProperties.contracts.entries.forEachIndexed { i, it ->
            startMaster(
                repeatInterval = REPEAT_INTERVAL,
                startDelay = (i * START_DELAY).toLong(),
                masterJobData = MasterJobData(
                    contractType = it.key,
                    address = it.value
                )
            )
        }
    }

    private fun startMaster(repeatInterval: Int, startDelay: Long, masterJobData: MasterJobData) {
        runCatching {
            schedule(repeatInterval, startDelay, masterJobData)
        }.onFailure {
            schedulerService.deleteJob(masterJobData.contractType.name, LogEnrichmentMasterJob::class.java.simpleName)
            schedule(repeatInterval, startDelay, masterJobData)
        }
    }

    private fun schedule(repeatInterval: Int, startDelay: Long, masterJobData: MasterJobData) {
        val jobClass = LogEnrichmentMasterJob::class.java

        schedulerService.schedule(
            jobClass = jobClass,
            name = masterJobData.contractType.name,
            group = jobClass.simpleName,
            data = masterJobData,
            triggerProperties = TriggerProperties(
                repeatCount = -1,
                repeatIntervalInSeconds = repeatInterval,
                priority = 15,
                startAt = LocalDateTime.now().plusSeconds(startDelay)
            ),
            logJobStart = false
        )
    }
}