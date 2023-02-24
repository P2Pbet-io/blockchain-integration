package com.p2pbet.client.web3.model.common

import com.p2pbet.client.web3.model.AbstractLog
import com.p2pbet.client.web3.model.LogEnumMapper
import com.p2pbet.client.web3.model.annotation.AddressTopic
import java.math.BigInteger

class TransferredOwnershipLog(
    private val contractAddressHidden: String,
    private val blockNumberHidden: BigInteger,
    private val transactionHashHidden: String,
    private val logTypeHidden: LogEnumMapper,
    @field:AddressTopic(0)
    val previousOwner: String,
    @field:AddressTopic(1)
    val newOwner: String,
) : AbstractLog(
    contractAddress = contractAddressHidden,
    blockNumber = blockNumberHidden,
    transactionHash = transactionHashHidden,
    logType = logTypeHidden
)