package com.p2pbet.service.async.auction

import com.p2pbet.service.model.BaseExecutionModel
import com.p2pbet.service.model.auction.CloseAuctionBetDTO
import com.p2pbet.service.model.auction.CreateAuctionBetDTO
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.DynamicArray
import org.web3j.abi.datatypes.DynamicStruct
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.utils.Convert
import org.web3j.utils.Convert.toWei
import java.time.ZoneOffset

object AuctionFunctionHelper {
    val formCreateAuctionHex: (BaseExecutionModel) -> String = {
        with(it as CreateAuctionBetDTO) {
            FunctionEncoder.encode(
                Function(
                    "createAuctionBet",
                    listOf(
                        DynamicStruct(
                            Utf8String(eventId),
                            Uint256(
                                lockTime.withOffsetSameInstant(ZoneOffset.UTC).toEpochSecond()
                            ),
                            Uint256(
                                expirationTime.withOffsetSameInstant(ZoneOffset.UTC).toEpochSecond()
                            ),
                            Uint256(
                                toWei(requestAmount, Convert.Unit.ETHER).toBigInteger()
                            )
                        )
                    ),
                    listOf(object : TypeReference<Uint256>() {})
                )
            )
        }
    }

    val formCloseAuctionHex: (BaseExecutionModel) -> String = {
        with(it as CloseAuctionBetDTO) {
            FunctionEncoder.encode(
                Function(
                    "closeAuctionBet",
                    listOf(
                        Uint256(betId), Utf8String(finalValue), DynamicArray(
                            Uint256::class.java,
                            joinIdsWon.map(::Uint256)
                        )
                    ),
                    listOf()
                )
            )
        }
    }
}