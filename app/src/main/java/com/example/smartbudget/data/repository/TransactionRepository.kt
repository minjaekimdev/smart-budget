package com.example.smartbudget.data.repository

import androidx.lifecycle.LiveData
import com.example.smartbudget.data.dao.TransactionDao
import com.example.smartbudget.data.model.TransactionEntity

class TransactionRepository(private val transactionDao: TransactionDao) {
    // 모든 데이터 가져오기 (LiveData)
    val allTransactions: LiveData<List<TransactionEntity>> = transactionDao.getAll()

    // 월별 데이터 가져오기
    fun getByMonth(month: String): LiveData<List<TransactionEntity>> {
        return transactionDao.getByMonth(month)
    }

    // 데이터 저장 (비동기 처리)
    suspend fun insert(transaction: TransactionEntity) {
        transactionDao.insert(transaction)
    }

    // 데이터 삭제 (비동기 처리)
    suspend fun delete(transaction: TransactionEntity) {
        transactionDao.delete(transaction)
    }
}