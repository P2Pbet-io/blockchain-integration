package com.p2pbet.service.model.auction

import com.p2pbet.persistance.entity.enums.ContractType
import com.p2pbet.service.model.BaseExecutionModel
import java.math.BigInteger

data class CloseAuctionBetDTO(
    val betId: Long,
    val finalValue: String,
    val joinIdsWon: List<BigInteger>
) : BaseExecutionModel(
    contractType = ContractType.AUCTION
)