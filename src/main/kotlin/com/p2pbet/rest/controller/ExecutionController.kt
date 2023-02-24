package com.p2pbet.rest.controller

import com.p2pbet.service.AsyncExecutionService
import com.p2pbet.service.model.ExecutionResponseDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ExecutionController(
    val asyncExecutionService: AsyncExecutionService
) {
    @GetMapping("/api/v1/execution/{id}")
    fun closeCustomBet(
        @PathVariable id: UUID
    ): ExecutionResponseDTO = asyncExecutionService.getExecution(id)
}