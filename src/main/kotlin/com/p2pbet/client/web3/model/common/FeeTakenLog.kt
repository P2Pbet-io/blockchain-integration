package com.p2pbet.client.web3.model.common

import com.p2pbet.client.web3.model.AbstractLog
import com.p2pbet.client.web3.model.LogEnumMapper
import com.p2pbet.client.web3.model.annotation.AddressTopic
import com.p2pbet.client.web3.model.annotation.BooleanValue
import com.p2pbet.client.web3.model.annotation.NumberValue
import java.math.BigInteger

class FeeTakenLog(
    private val contractAddressHidden: String,
    private val blockNumberHidden: BigInteger,
    private val transactionHashHidden: String,
    private val logTypeHidden: LogEnumMapper,
    @field:NumberValue(0)
    val amount: BigInteger,
    @field:AddressTopic(0)
    val targetAddress: String,
    @field:BooleanValue(1)
    val isAlternative: Boolean,
) : AbstractLog(
    contractAddress = contractAddressHidden,
    blockNumber = blockNumberHidden,
    transactionHash = transactionHashHidden,
    logType = logTypeHidden
)