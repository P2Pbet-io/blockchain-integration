package com.p2pbet.client.web3.model.binary

import com.p2pbet.client.web3.model.AbstractLog
import com.p2pbet.client.web3.model.LogEnumMapper
import com.p2pbet.client.web3.model.annotation.AddressTopic
import com.p2pbet.client.web3.model.annotation.NumberValue
import com.p2pbet.client.web3.model.annotation.StringValue
import java.math.BigInteger

class BinaryBetCreatedLog(
    private val contractAddressHidden: String,
    private val blockNumberHidden: BigInteger,
    private val transactionHashHidden: String,
    private val logTypeHidden: LogEnumMapper,
    @field:NumberValue(0)
    val id: BigInteger,
    @field:StringValue(1)
    val eventId: String,
    @field:NumberValue(2)
    val lockTime: BigInteger,
    @field:NumberValue(3)
    val expirationTime: BigInteger,
    @field:AddressTopic(0)
    val creator: String
) : AbstractLog(
    contractAddress = contractAddressHidden,
    blockNumber = blockNumberHidden,
    transactionHash = transactionHashHidden,
    logType = logTypeHidden
)