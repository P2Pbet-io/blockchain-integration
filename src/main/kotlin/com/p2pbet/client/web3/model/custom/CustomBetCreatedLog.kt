package com.p2pbet.client.web3.model.custom

import com.p2pbet.client.web3.model.AbstractLog
import com.p2pbet.client.web3.model.LogEnumMapper
import com.p2pbet.client.web3.model.annotation.AddressTopic
import com.p2pbet.client.web3.model.annotation.BooleanValue
import com.p2pbet.client.web3.model.annotation.NumberValue
import com.p2pbet.client.web3.model.annotation.StringValue
import java.math.BigInteger

class CustomBetCreatedLog(
    private val contractAddressHidden: String,
    private val blockNumberHidden: BigInteger,
    private val transactionHashHidden: String,
    private val logTypeHidden: LogEnumMapper,
    @field:NumberValue(0)
    val id: BigInteger,
    @field:StringValue(1)
    val eventId: String,
    @field:BooleanValue(2)
    val hidden: Boolean,
    @field:NumberValue(3)
    val lockTime: BigInteger,
    @field:NumberValue(4)
    val expirationTime: BigInteger,
    @field:StringValue(5)
    val targetValue: String,
    @field:BooleanValue(6)
    val targetSide: Boolean,
    @field:NumberValue(7)
    val coefficient: BigInteger,
    @field:AddressTopic(0)
    val creator: String
) : AbstractLog(
    contractAddress = contractAddressHidden,
    blockNumber = blockNumberHidden,
    transactionHash = transactionHashHidden,
    logType = logTypeHidden
)