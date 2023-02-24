package com.p2pbet.persistance.repository

import com.p2pbet.persistance.entity.GovernanceWalletEntity
import com.p2pbet.persistance.entity.enums.ContractType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface GovernanceWalletRepository : JpaRepository<GovernanceWalletEntity, String> {
    fun existsByType(type: ContractType): Boolean

    @Query(
        """
            SELECT * FROM governance_wallet
             WHERE type = :type 
             FOR UPDATE
            """,
        nativeQuery = true
    )
    fun getByType(type: String): GovernanceWalletEntity
}