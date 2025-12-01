package com.example.smartbudget.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// 변수 이름 통일
@Entity(tableName = "transaction_table")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String,   // "수입" or "지출"
    val amount: Int,    // 금액 (ex. 30000)
    val date: String,   // 날짜 (ex. 2025-11-30)
    val note: String,   // 내용
)