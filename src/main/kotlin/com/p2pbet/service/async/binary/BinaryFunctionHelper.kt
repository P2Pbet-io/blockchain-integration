package com.p2pbet.service.async.binary

import com.p2pbet.service.model.BaseExecutionModel
import com.p2pbet.service.model.binary.CloseBinaryBetDTO
import com.p2pbet.service.model.binary.CreateBinaryBetDTO
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.DynamicStruct
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import java.time.ZoneOffset

object BinaryFunctionHelper {
    val formCreateBinaryHex: (BaseExecutionModel) -> String = {
        with(it as CreateBinaryBetDTO) {
            FunctionEncoder.encode(
                Function(
                    "createBinaryBet",
                    listOf(
                        DynamicStruct(
                            Utf8String(eventId),
                            Uint256(
                                lockTime.withOffsetSameInstant(ZoneOffset.UTC).toEpochSecond()
                            ),
                            Uint256(
                                expirationTime.withOffsetSameInstant(ZoneOffset.UTC).toEpochSecond()
                            )
                        )
                    ),
                    listOf(object : TypeReference<Uint256>() {})
                )
            )
        }
    }

    val formCloseBinaryHex: (BaseExecutionModel) -> String = {
        with(it as CloseBinaryBetDTO) {
            FunctionEncoder.encode(
                Function(
                    "closeBinaryBet",
                    listOf(Uint256(betId), Utf8String(lockedValue), Utf8String(finalValue), Bool(targetSideWon)),
                    listOf()
                )
            )
        }
    }
}