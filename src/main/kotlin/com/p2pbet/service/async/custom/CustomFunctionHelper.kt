package com.p2pbet.service.async.custom

import com.p2pbet.service.model.BaseExecutionModel
import com.p2pbet.service.model.custom.CloseCustomBetDTO
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256

object CustomFunctionHelper {
    val formCloseCustomHex: (BaseExecutionModel) -> String = {
        with(it as CloseCustomBetDTO) {
            FunctionEncoder.encode(
                Function(
                    "closeCustomBet",
                    listOf(Uint256(betId), Utf8String(finalValue), Bool(targetSideWon)),
                    listOf()
                )
            )
        }
    }
}