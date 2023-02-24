package com.p2pbet.client.web3.model.jackpot

import com.p2pbet.client.web3.model.AbstractLog
import com.p2pbet.client.web3.model.LogEnumMapper
import com.p2pbet.client.web3.model.annotation.AddressTopic
import com.p2pbet.client.web3.model.annotation.BooleanValue
import com.p2pbet.client.web3.model.annotation.NumberValue
import java.math.BigInteger

class JackpotBetPrizeTakenLog(
    private val contractAddressHidden: String,
    private val blockNumberHidden: BigInteger,
    private val transactionHashHidden: String,
    private val logTypeHidden: LogEnumMapper,
    @field:NumberValue(0)
    val betId: BigInteger,
    @field:AddressTopic(0)
    val clientAddress: String,
    @field:NumberValue(1)
    val amount: BigInteger,
    @field:BooleanValue(2)
    val useAlterFee: Boolean
) : AbstractLog(
    contractAddress = contractAddressHidden,
    blockNumber = blockNumberHidden,
    transactionHash = transactionHashHidden,
    logType = logTypeHidden
)