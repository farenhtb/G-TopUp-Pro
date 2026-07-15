package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM topup_transactions ORDER BY transactionTime DESC")
    fun getAllTransactions(): Flow<List<TopUpTransaction>>

    @Query("SELECT * FROM topup_transactions WHERE id = :id LIMIT 1")
    fun getTransactionById(id: Int): Flow<TopUpTransaction?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TopUpTransaction): Long

    @Query("UPDATE topup_transactions SET paymentStatus = :status WHERE id = :id")
    suspend fun updateTransactionStatus(id: Int, status: String)

    @Query("DELETE FROM topup_transactions WHERE id = :id")
    suspend fun deleteTransaction(id: Int)
}
