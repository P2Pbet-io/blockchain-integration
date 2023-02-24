package com.p2pbet.client.web3.model

import com.p2pbet.client.web3.model.annotation.*
import com.p2pbet.client.web3.properties.LogProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import org.web3j.protocol.core.methods.response.EthLog
import java.math.BigInteger
import kotlin.reflect.full.primaryConstructor

@Component
@EnableConfigurationProperties(LogProperties::class)
class LogMapper(
    val logProperties: LogProperties
) {
    fun extractLog(event: EthLog.LogObject): AbstractLog =
        with(getClassByTopic(event.topics[0])) {
            val dataList = event.data
                .subSequence(2, event.data.length)
                .chunked(64)

            val arguments = mutableListOf(event.address, event.blockNumber, event.transactionHash, this,
                *extendedClass.java.declaredFields.toList()
                    .filter { member -> member.annotations.isNotEmpty() }
                    .map { member ->
                        when (member.annotations[0].annotationClass) {
                            AddressValue::class -> extractAddress(dataList[(member.annotations[0] as AddressValue).position])
                            AddressTopic::class -> extractTopicAddress(event.topics[(member.annotations[0] as AddressTopic).position + 1])
                            NumberValue::class -> extractNumber(dataList[(member.annotations[0] as NumberValue).position])
                            BooleanValue::class -> extractBoolean(dataList[(member.annotations[0] as BooleanValue).position])
                            StringValue::class -> extractString(
                                row = dataList[(member.annotations[0] as StringValue).position],
                                dataList = dataList
                            )

                            ListNumberValue::class -> extractListNumber(
                                row = dataList[(member.annotations[0] as ListNumberValue).position],
                                dataList = dataList,
                            )

                            else -> null
                        }

                    }
                    .toTypedArray()
            ).toTypedArray()


            extendedClass.primaryConstructor!!.call(*arguments)
        }


    private fun extractAddress(row: String) = "0x" + row.substring(24, 64)
    private fun extractTopicAddress(row: String) = "0x" + row.substring(26, 66)
    private fun extractNumber(row: String) = row.toBigInteger(radix = 16)
    private fun extractBoolean(row: String) = (row.toInt() == 1)
    private fun extractString(row: String, dataList: List<String>): String {
        val offset = (extractNumber(row).toLong() / 32).toInt()
        val length = extractNumber(dataList[offset]).toLong()
        val builder = StringBuilder()
        for (i in 0 until length) {
            val stringRow = dataList[(offset + 1 + i / 32).toInt()]
            builder.append(
                extractNumber(
                    row = (stringRow[i.toInt() % 32 * 2].plus(stringRow[i.toInt() % 32 * 2 + 1].toString()))
                ).toInt().toChar()
            )
        }
        return builder.toString()
    }

    private fun extractListNumber(row: String, dataList: List<String>): List<BigInteger> {
        val offset = (extractNumber(row).toLong() / 32).toInt()
        val length = extractNumber(dataList[offset]).toInt()

        val result = mutableListOf<BigInteger>()
        for (i in offset until offset + length) {
            result.add(extractNumber(dataList[i + 1]).toLong().toBigInteger())
        }
        return result
    }

    private fun getClassByTopic(topic: String) =
        logProperties
            .eventMapping
            .entries
            .firstOrNull() {
                it.value == topic
            }
            ?.key
            ?: throw RuntimeException("No existence topic found: $topic")
}