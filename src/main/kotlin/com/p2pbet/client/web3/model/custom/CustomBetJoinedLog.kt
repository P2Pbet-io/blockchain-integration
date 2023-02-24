package com.p2pbet.client.web3.model.custom

import com.p2pbet.client.web3.model.AbstractLog
import com.p2pbet.client.web3.model.LogEnumMapper
import com.p2pbet.client.web3.model.annotation.AddressTopic
import com.p2pbet.client.web3.model.annotation.BooleanValue
import com.p2pbet.client.web3.model.annotation.NumberValue
import java.math.BigInteger

class CustomBetJoinedLog(
    private val contractAddressHidden: String,
    private val blockNumberHidden: BigInteger,
    private val transactionHashHidden: String,
    private val logTypeHidden: LogEnumMapper,
    @field:BooleanValue(0)
    val side: Boolean,
    @field:NumberValue(1)
    val mainAmount: BigInteger,
    @field:AddressTopic(0)
    val client: String,
    @field:NumberValue(2)
    val betId: BigInteger,
    @field:NumberValue(3)
    val joinId: BigInteger,
    @field:NumberValue(4)
    val joinIdRef: BigInteger
) : AbstractLog(
    contractAddress = contractAddressHidden,
    blockNumber = blockNumberHidden,
    transactionHash = transactionHashHidden,
    logType = logTypeHidden
)