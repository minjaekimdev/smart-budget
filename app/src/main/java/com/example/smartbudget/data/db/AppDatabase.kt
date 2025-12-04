package com.example.smartbudget.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smartbudget.data.dao.TransactionDao
import com.example.smartbudget.data.model.TransactionEntity

// 엔티티와 버전 정의
@Database(entities = [TransactionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // DAO를 반환하는 추상 함수
    abstract fun transactionDao(): TransactionDao

    // 싱글톤 패턴 적용
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // DB 인스턴스 생성 및 반환
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_budget_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}