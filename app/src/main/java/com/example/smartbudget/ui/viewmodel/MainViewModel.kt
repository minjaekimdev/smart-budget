import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.smartbudget.data.db.AppDatabase
import com.example.smartbudget.data.model.TransactionEntity
import com.example.smartbudget.data.repository.TransactionRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TransactionRepository
    val allTransactions: LiveData<List<TransactionEntity>>

    init {
        // 1. DB 인스턴스에서 DAO 꺼내기
        val transactionDao = AppDatabase.getDatabase(application).transactionDao()
        // 2. Repository 생성
        repository = TransactionRepository(transactionDao)
        // 3. 데이터 연결
        allTransactions = repository.allTransactions
    }

    // ... 나머지 함수들 (addTransaction 등)은 Repository 함수를 호출하도록 작성
}