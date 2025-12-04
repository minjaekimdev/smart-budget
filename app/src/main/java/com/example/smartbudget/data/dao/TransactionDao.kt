package com.example.smartbudget.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.smartbudget.data.model.TransactionEntity

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transaction_table ORDER BY date DESC")
    fun getAll(): LiveData<List<TransactionEntity>>

    // yearMonth에는 "2025-12" 형식의 문자열이 들어옴
    // 여기에 와일드카드 "%"를 붙여 해당 월에 해당하는 데이터를 찾음
    @Query("SELECT * FROM transaction_table WHERE date LIKE :yearMonth || '%'")
    fun getByMonth(yearMonth: String): LiveData<List<TransactionEntity>>

    // 오래 걸리는 작업이므로 suspend(백그라운드에서 비동기 처리)
    @Insert
    suspend fun insert(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)
}