package com.p2pbet.client.web3.model.custom

import com.p2pbet.client.web3.model.AbstractLog
import com.p2pbet.client.web3.model.LogEnumMapper
import com.p2pbet.client.web3.model.annotation.BooleanValue
import com.p2pbet.client.web3.model.annotation.NumberValue
import com.p2pbet.client.web3.model.annotation.StringValue
import java.math.BigInteger

class CustomBetClosedLog(
    private val contractAddressHidden: String,
    private val blockNumberHidden: BigInteger,
    private val transactionHashHidden: String,
    private val logTypeHidden: LogEnumMapper,
    @field:NumberValue(0)
    val betId: BigInteger,
    @field:StringValue(1)
    val finalValue: String,
    @field:BooleanValue(2)
    val targetSideWon: Boolean
) : AbstractLog(
    contractAddress = contractAddressHidden,
    blockNumber = blockNumberHidden,
    transactionHash = transactionHashHidden,
    logType = logTypeHidden
)