package com.p2pbet.rest.controller

import com.p2pbet.service.AsyncExecutionService
import com.p2pbet.service.model.ExecutionResponseDTO
import com.p2pbet.service.model.auction.CloseAuctionBetDTO
import com.p2pbet.service.model.auction.CreateAuctionBetDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuctionBetController(
    val asyncExecutionService: AsyncExecutionService
) {
    @PostMapping("/api/v1/auction/create")
    fun createAuctionBet(
        @RequestBody request: CreateAuctionBetDTO
    ): ExecutionResponseDTO = asyncExecutionService.startAsyncExecution(request)

    @PostMapping("/api/v1/auction/close")
    fun closeAuctionBet(
        @RequestBody request: CloseAuctionBetDTO
    ): ExecutionResponseDTO = asyncExecutionService.startAsyncExecution(request)
}