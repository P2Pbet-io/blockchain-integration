package com.p2pbet.client.signification.api

import com.p2pbet.client.signification.api.model.SignEthereumTransactionRequestDTO
import com.p2pbet.client.signification.api.model.SignEthereumTransactionResponseDTO
import com.p2pbet.client.signification.api.model.SignificationAddressResponseDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

interface SignificationApi {
    @PostMapping("/api/v1/ETH/addresses")
    fun createAddress(): SignificationAddressResponseDTO

    @PostMapping("/api/v1/ETH/transactions/sign")
    fun signEthereumTransaction(
        @RequestBody request: SignEthereumTransactionRequestDTO
    ): SignEthereumTransactionResponseDTO
}