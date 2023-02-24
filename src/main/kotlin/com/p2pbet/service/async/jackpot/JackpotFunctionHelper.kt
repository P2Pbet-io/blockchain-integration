package com.p2pbet.service.async.jackpot

import com.p2pbet.service.model.BaseExecutionModel
import com.p2pbet.service.model.jackpot.CloseJackpotBetDTO
import com.p2pbet.service.model.jackpot.CreateJackpotBetDTO
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.DynamicStruct
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.utils.Convert
import org.web3j.utils.Convert.toWei
import java.time.ZoneOffset

object JackpotFunctionHelper {
    val formCreateJackpotHex: (BaseExecutionModel) -> String = {
        with(it as CreateJackpotBetDTO) {
            FunctionEncoder.encode(
                Function(
                    "createJackpotBet",
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

    val formCloseJackpotHex: (BaseExecutionModel) -> String = {
        with(it as CloseJackpotBetDTO) {
            FunctionEncoder.encode(
                Function(
                    "closeJackpotBet",
                    listOf(Uint256(betId), Utf8String(finalValue)),
                    listOf()
                )
            )
        }
    }
}