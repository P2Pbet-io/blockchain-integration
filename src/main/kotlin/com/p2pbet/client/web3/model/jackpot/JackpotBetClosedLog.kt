package com.p2pbet.client.web3.model.jackpot

import com.p2pbet.client.web3.model.AbstractLog
import com.p2pbet.client.web3.model.LogEnumMapper
import com.p2pbet.client.web3.model.annotation.NumberValue
import com.p2pbet.client.web3.model.annotation.StringValue
import java.math.BigInteger

class JackpotBetClosedLog(
    private val contractAddressHidden: String,
    private val blockNumberHidden: BigInteger,
    private val transactionHashHidden: String,
    private val logTypeHidden: LogEnumMapper,
    @field:NumberValue(0)
    val betId: BigInteger,
    @field:StringValue(1)
    val finalValue: String,
    @field:NumberValue(2)
    val firstWonSize: BigInteger,
    @field:NumberValue(3)
    val secondWonSize: BigInteger,
    @field:NumberValue(4)
    val thirdWonSize: BigInteger,
    @field:NumberValue(5)
    val totalRaffled: BigInteger
) : AbstractLog(
    contractAddress = contractAddressHidden,
    blockNumber = blockNumberHidden,
    transactionHash = transactionHashHidden,
    logType = logTypeHidden
)