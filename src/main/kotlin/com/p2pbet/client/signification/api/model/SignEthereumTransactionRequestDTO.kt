package com.p2pbet.client.signification.api.model

import java.math.BigInteger

data class SignEthereumTransactionRequestDTO(
    val from: String,
    val to: String,
    val amount: BigInteger,
    val data: String,
    val nonce: BigInteger,
    val gasLimit: BigInteger,
    val gasPrice: BigInteger
)
