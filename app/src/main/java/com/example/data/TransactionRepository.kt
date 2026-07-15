package com.example.data

import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions: Flow<List<TopUpTransaction>> = transactionDao.getAllTransactions()

    fun getTransactionById(id: Int): Flow<TopUpTransaction?> {
        return transactionDao.getTransactionById(id)
    }

    suspend fun insert(transaction: TopUpTransaction): Long {
        return transactionDao.insertTransaction(transaction)
    }

    suspend fun updateStatus(id: Int, status: String) {
        transactionDao.updateTransactionStatus(id, status)
    }

    suspend fun delete(id: Int) {
        transactionDao.deleteTransaction(id)
    }
}
