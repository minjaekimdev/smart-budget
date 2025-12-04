package com.example.smartbudget.data.repository

import androidx.lifecycle.LiveData
import com.example.smartbudget.data.dao.TransactionDao
import com.example.smartbudget.data.model.TransactionEntity

class TransactionRepository(private val transactionDao: TransactionDao) {

    // 전체 내역 조회
    val allTransactions: LiveData<List<TransactionEntity>> =
        transactionDao.getAll()

    // 월별 조회 ("2025-12" 을 그대로 넘김)
    fun getByMonth(month: String): LiveData<List<TransactionEntity>> {
        return transactionDao.getByMonth(month)
    }

    // 저장
    suspend fun insert(transaction: TransactionEntity) {
        transactionDao.insert(transaction)
    }

    // 삭제
    suspend fun delete(transaction: TransactionEntity) {
        transactionDao.delete(transaction)
    }
}
