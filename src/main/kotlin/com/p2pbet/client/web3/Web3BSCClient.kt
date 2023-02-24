package com.p2pbet.client.web3

import com.p2pbet.client.web3.model.LogMapper
import mu.KLogger
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.web3j.contracts.eip20.generated.ERC20
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.response.EthLog
import org.web3j.tx.ReadonlyTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger

@Component
class Web3BSCClient(
    val web3Client: Web3j,
    val logMapper: LogMapper
) {
    private val logger: KLogger = KotlinLogging.logger { }

    fun getLastBlock(): BigInteger = try {
        web3Client
            .ethBlockNumber()
            .send()
            .blockNumber
    } catch (e: Exception) {
        throw RuntimeException("Can not get BSC block number. Error: ${e.message}", e)
    }

    fun getTransaction(transactionHash: String) = try {
        web3Client
            .ethGetTransactionByHash(transactionHash)
            .send()
            .transaction.get()
    } catch (e: Exception) {
        throw RuntimeException("Can not get BSC transaction. Error: ${e.message}", e)
    }

    fun getMaxGasLimit(): BigInteger = try {
        web3Client
            .ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
            .send()
            .block
            .gasLimit
    } catch (e: Exception) {
        throw RuntimeException("Can not get BSC max gas limit. Error: ${e.message}", e)
    }

    fun getTransactionReceipt(transactionHash: String) = try {
        web3Client
            .ethGetTransactionReceipt(transactionHash)
            .send()
            .transactionReceipt
            .get()
    } catch (e: Exception) {
        throw RuntimeException("Can not get BSC transaction receipt. Error: ${e.message}", e)
    }

    fun getGasPrice(): BigInteger = try {
        web3Client
            .ethGasPrice()
            .send()
            .gasPrice
    } catch (e: Exception) {
        throw RuntimeException("Can not get BSC gas price. Error: ${e.message}", e)
    }

    fun getBalance(address: String, blockNumber: BigInteger): BigInteger = try {
        web3Client
            .ethGetBalance(address, DefaultBlockParameter.valueOf(blockNumber))
            .send()
            .balance
    } catch (e: Exception) {
        throw RuntimeException("Can not get BSC gas price. Error: ${e.message}", e)
    }

    fun sendTransaction(transactionHex: String): String {
        try {
            return web3Client
                .ethSendRawTransaction(transactionHex)
                .send()
                .transactionHash
        } catch (e: Exception) {
            throw RuntimeException("Can not send BSC transaction. Error: ${e.message}", e)
        }
    }

    fun getLogs(
        address: String,
        fromBlock: Long,
        toBlock: Long
    ) = try {
        web3Client
            .ethGetLogs(
                EthFilter(
                    DefaultBlockParameter.valueOf(fromBlock.toBigInteger()),
                    DefaultBlockParameter.valueOf(toBlock.toBigInteger()),
                    address
                )
            )
            .send()
            .logs
            .map {
                it as EthLog.LogObject
            }.mapNotNull {
                logMapper.runCatching {
                    extractLog(it)
                }.getOrElse {
                    logger.error { "Can't extract log from fetch: ${it.message}" }
                    null
                }
            }
    } catch (e: Exception) {
        throw RuntimeException("Can not get BSC logs. Error: ${e.message}", e)
    }

    private fun getToken(contractAddress: String, fromAddress: String): ERC20 =
        ERC20.load(
            contractAddress,
            web3Client,
            ReadonlyTransactionManager(web3Client, fromAddress),
            DefaultGasProvider()
        )
}