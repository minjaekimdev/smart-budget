package com.example.smartbudget.ui.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.smartbudget.data.db.AppDatabase
import com.example.smartbudget.data.model.TransactionEntity
import com.example.smartbudget.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository

    // 화면(Fragment)에서 관찰할 데이터 (전체 내역)
    val allTransactions: LiveData<List<TransactionEntity>>

    init {
        // 1. DB 인스턴스에서 DAO 꺼내기
        // (application context를 사용해 DB를 엽니다)
        val transactionDao = AppDatabase.getInstance(application).transactionDao()

        // 2. Repository 생성 (DAO를 넘겨줌)
        repository = TransactionRepository(transactionDao)

        // 3. 데이터 연결 (Repository의 데이터를 뷰모델 변수에 연결)
        // Fragment까 allTransactions를 관찰할 수 있음
        allTransactions = repository.allTransactions
    }

    // 저장 (AddTransactionFragment에서 호출)
    fun addTransaction(transaction: TransactionEntity) = viewModelScope.launch {
        repository.insert(transaction)
    }

    // 삭제 (HomeFragment에서 호출)
    fun deleteTransaction(transaction: TransactionEntity) = viewModelScope.launch {
        repository.delete(transaction)
    }

    // 월별 데이터 가져오기 (StatisticsFragment에서 호출)
    fun getMonthlyTransactions(month: String): LiveData<List<TransactionEntity>> {
        return repository.getByMonth(month)
    }
}
