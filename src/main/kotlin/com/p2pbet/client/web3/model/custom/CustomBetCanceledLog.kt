package com.p2pbet.client.web3.model.custom

import com.p2pbet.client.web3.model.AbstractLog
import com.p2pbet.client.web3.model.LogEnumMapper
import com.p2pbet.client.web3.model.annotation.AddressTopic
import com.p2pbet.client.web3.model.annotation.NumberValue
import java.math.BigInteger

class CustomBetCanceledLog(
    private val contractAddressHidden: String,
    private val blockNumberHidden: BigInteger,
    private val transactionHashHidden: String,
    private val logTypeHidden: LogEnumMapper,
    @field:NumberValue(0)
    val betId: BigInteger,
    @field:AddressTopic(0)
    val client: String,
    @field:NumberValue(1)
    val joinIdRef: BigInteger,
    @field:NumberValue(2)
    val mainTokenRefunded: BigInteger
) : AbstractLog(
    contractAddress = contractAddressHidden,
    blockNumber = blockNumberHidden,
    transactionHash = transactionHashHidden,
    logType = logTypeHidden
)