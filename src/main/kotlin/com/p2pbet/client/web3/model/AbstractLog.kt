package com.p2pbet.client.web3.model

import java.math.BigInteger

abstract class AbstractLog(
    val contractAddress: String,
    val blockNumber: BigInteger,
    val transactionHash: String,
    val logType: LogEnumMapper
)
