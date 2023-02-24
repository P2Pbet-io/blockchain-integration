package com.p2pbet.service.async

import com.p2pbet.service.async.auction.AuctionFunctionHelper
import com.p2pbet.service.async.binary.BinaryFunctionHelper
import com.p2pbet.service.async.custom.CustomFunctionHelper
import com.p2pbet.service.async.jackpot.JackpotFunctionHelper
import com.p2pbet.service.model.BaseExecutionModel
import com.p2pbet.service.model.auction.CloseAuctionBetDTO
import com.p2pbet.service.model.auction.CreateAuctionBetDTO
import com.p2pbet.service.model.binary.CloseBinaryBetDTO
import com.p2pbet.service.model.binary.CreateBinaryBetDTO
import com.p2pbet.service.model.custom.CloseCustomBetDTO
import com.p2pbet.service.model.jackpot.CloseJackpotBetDTO
import com.p2pbet.service.model.jackpot.CreateJackpotBetDTO
import kotlin.reflect.KClass

object AsyncOperationMapper {
    private val mappers: Map<KClass<out BaseExecutionModel>, (model: BaseExecutionModel) -> String> = mapOf(
        CloseCustomBetDTO::class to CustomFunctionHelper.formCloseCustomHex,
        CreateBinaryBetDTO::class to BinaryFunctionHelper.formCreateBinaryHex,
        CloseBinaryBetDTO::class to BinaryFunctionHelper.formCloseBinaryHex,
        CreateAuctionBetDTO::class to AuctionFunctionHelper.formCreateAuctionHex,
        CloseAuctionBetDTO::class to AuctionFunctionHelper.formCloseAuctionHex,
        CreateJackpotBetDTO::class to JackpotFunctionHelper.formCreateJackpotHex,
        CloseJackpotBetDTO::class to JackpotFunctionHelper.formCloseJackpotHex,
    )

    fun <T> T.prepareEncodedTxData(): String where T : BaseExecutionModel = mappers[this::class]?.invoke(this)!!
}