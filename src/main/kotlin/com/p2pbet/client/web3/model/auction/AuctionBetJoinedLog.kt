package com.p2pbet.client.web3.model.auction

import com.p2pbet.client.web3.model.AbstractLog
import com.p2pbet.client.web3.model.LogEnumMapper
import com.p2pbet.client.web3.model.annotation.AddressTopic
import com.p2pbet.client.web3.model.annotation.NumberValue
import com.p2pbet.client.web3.model.annotation.StringValue
import java.math.BigInteger

class AuctionBetJoinedLog(
    private val contractAddressHidden: String,
    private val blockNumberHidden: BigInteger,
    private val transactionHashHidden: String,
    private val logTypeHidden: LogEnumMapper,
    @field:AddressTopic(0)
    val client: String,
    @field:NumberValue(0)
    val betId: BigInteger,
    @field:NumberValue(1)
    val joinId: BigInteger,
    @field:NumberValue(2)
    val joinIdRef: BigInteger,
    @field:StringValue(3)
    val targetValue: String
) : AbstractLog(
    contractAddress = contractAddressHidden,
    blockNumber = blockNumberHidden,
    transactionHash = transactionHashHidden,
    logType = logTypeHidden
)