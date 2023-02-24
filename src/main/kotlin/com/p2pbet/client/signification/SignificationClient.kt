package com.p2pbet.client.signification

import com.p2pbet.client.signification.api.SignificationApi
import com.p2pbet.client.signification.api.model.SignEthereumTransactionRequestDTO
import org.springframework.stereotype.Component
import java.math.BigInteger

@Component
class SignificationClient(
    private val significationApi: SignificationApi
) {
    fun createAddress() = significationApi.createAddress()

    fun signTransaction(
        from: String,
        to: String,
        amount: BigInteger,
        data: String,
        nonce: BigInteger,
        gasLimit: BigInteger,
        gasPrice: BigInteger
    ) = significationApi.signEthereumTransaction(
        request = SignEthereumTransactionRequestDTO(
            from = from,
            to = to,
            amount = amount,
            data = data,
            nonce = nonce,
            gasLimit = gasLimit,
            gasPrice = gasPrice
        )
    )
}