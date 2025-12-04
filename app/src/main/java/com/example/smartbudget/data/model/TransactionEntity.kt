package com.example.smartbudget.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val type: String,        // INCOME / EXPENSE
    val date: String,        // 예: "2025-12-04"
    val amount: Int,
    val note: String
)
