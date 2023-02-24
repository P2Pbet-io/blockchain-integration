package com.p2pbet.rest.controller

import com.p2pbet.service.AsyncExecutionService
import com.p2pbet.service.model.ExecutionResponseDTO
import com.p2pbet.service.model.custom.CloseCustomBetDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomBetController(
    val asyncExecutionService: AsyncExecutionService
) {
    @PostMapping("/api/v1/custom/close")
    fun closeCustomBet(
        @RequestBody request: CloseCustomBetDTO
    ): ExecutionResponseDTO = asyncExecutionService.startAsyncExecution(request)
}