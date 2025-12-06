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

    @Query("SELECT * FROM transaction_table WHERE date LIKE :yearMonth || '%' ORDER BY date DESC")
    fun getByMonth(yearMonth: String): LiveData<List<TransactionEntity>>

    @Insert
    suspend fun insert(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)
}
