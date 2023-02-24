package com.p2pbet.rest.controller

import com.p2pbet.service.AsyncExecutionService
import com.p2pbet.service.model.ExecutionResponseDTO
import com.p2pbet.service.model.jackpot.CloseJackpotBetDTO
import com.p2pbet.service.model.jackpot.CreateJackpotBetDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class JackpotBetController(
    val asyncExecutionService: AsyncExecutionService
) {
    @PostMapping("/api/v1/jackpot/create")
    fun createJackpotBet(
        @RequestBody request: CreateJackpotBetDTO
    ): ExecutionResponseDTO = asyncExecutionService.startAsyncExecution(request)

    @PostMapping("/api/v1/jackpot/close")
    fun closeJackpotBet(
        @RequestBody request: CloseJackpotBetDTO
    ): ExecutionResponseDTO = asyncExecutionService.startAsyncExecution(request)
}