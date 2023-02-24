package com.p2pbet.service

import com.p2pbet.client.signification.api.SignificationApi
import com.p2pbet.client.web3.Web3BSCClient
import com.p2pbet.persistance.entity.GovernanceWalletEntity
import com.p2pbet.persistance.entity.enums.ContractType
import com.p2pbet.persistance.repository.GovernanceWalletRepository
import com.p2pbet.scheduler.master.properties.MasterLogProperties
import mu.KLogger
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.web3j.utils.Convert
import org.web3j.utils.Convert.fromWei
import java.math.BigDecimal
import javax.annotation.PostConstruct

@Service
class GovernanceWalletService(
    val governanceWalletRepository: GovernanceWalletRepository,
    val masterLogProperties: MasterLogProperties,
    val significationApi: SignificationApi,
    val web3BSCClient: Web3BSCClient
) {
    private val logger: KLogger = KotlinLogging.logger { }

    @PostConstruct
    fun initWallets() {
        logger.info("\u001B[32;1m Check governance wallets.' \u001B[0m")
        masterLogProperties.contracts.entries
            .filterNot { governanceWalletRepository.existsByType(it.key) }
            .forEach {
                val address = significationApi.createAddress().address
                governanceWalletRepository.save(
                    GovernanceWalletEntity(
                        address = address,
                        nonce = 0,
                        balance = BigDecimal.ZERO,
                        type = it.key,
                        controlContract = it.value
                    )
                )
                logger.info("\u001B[32;1m Create governance wallet for ${it.key}. Address: $address.' \u001B[0m")
            }
        logger.info("\u001B[32;1m Governance wallets loaded.' \u001B[0m")
    }

    @Transactional
    fun getGovernanceWallet(type: ContractType) = governanceWalletRepository.getByType(type.name)

    @Transactional
    fun incrementAndSave(governanceWallet: GovernanceWalletEntity) = governanceWallet
        .apply {
            nonce++
        }
        .apply(governanceWalletRepository::save)

    @Transactional
    fun refreshBalance(contractType: ContractType, blockNumber: Long) {
        val wallet = getGovernanceWallet(contractType)
        wallet.balance = fromWei(
            web3BSCClient.getBalance(wallet.address, blockNumber.toBigInteger()).toBigDecimal(),
            Convert.Unit.ETHER
        )
        governanceWalletRepository.save(wallet)
    }
}