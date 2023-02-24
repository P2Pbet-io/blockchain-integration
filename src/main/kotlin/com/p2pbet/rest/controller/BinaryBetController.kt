package com.p2pbet.rest.controller

import com.p2pbet.service.AsyncExecutionService
import com.p2pbet.service.model.ExecutionResponseDTO
import com.p2pbet.service.model.binary.CloseBinaryBetDTO
import com.p2pbet.service.model.binary.CreateBinaryBetDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BinaryBetController(
    val asyncExecutionService: AsyncExecutionService
) {
    @PostMapping("/api/v1/binary/create")
    fun createBinaryBet(
        @RequestBody request: CreateBinaryBetDTO
    ): ExecutionResponseDTO = asyncExecutionService.startAsyncExecution(request)

    @PostMapping("/api/v1/binary/close")
    fun closeBinaryBet(
        @RequestBody request: CloseBinaryBetDTO
    ): ExecutionResponseDTO = asyncExecutionService.startAsyncExecution(request)
}