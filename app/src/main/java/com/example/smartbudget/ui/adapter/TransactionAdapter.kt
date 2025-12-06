package com.example.smartbudget.ui.adapter
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartbudget.R
import com.example.smartbudget.data.model.TransactionEntity
import java.text.DecimalFormat

// 데이터베이스에서 가져온 거래내역 리스트를 사용자 인터페이스의 스크롤 가능한 목록 형태로 변환하여 화면에 표시
class TransactionAdapter(
    // 어댑터 내애서만 접근 가능한(private) 불변(val) 변수
    // 이 함수가 호출될 때 TransactionEntity 타입의 객체 하나를 인자로 받음(삭제대상)
    // -> Unit: 함수가 실행된 후 어떤 값도 반환하지 않음
    // 즉, 어떤 거래 내역을 삭제해야 하는지 알려주는 코드를 담는 상자
    private val onDeleteClick: (TransactionEntity) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    // list: TransactionEntity 객체들을 담는 리스트
    private var list = emptyList<TransactionEntity>()

    // 2. 숫자 포맷 (1000 -> 1,000)
    private val decimalFormat = DecimalFormat("#,###")

    // 3. 뷰홀더: item_transaction.xml의 텍스트뷰들을 잡아두는 역할
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvType: TextView = view.findViewById(R.id.tv_type)
        val tvAmount: TextView = view.findViewById(R.id.tv_amount)
        val tvDate: TextView = view.findViewById(R.id.tv_date)
        val tvNote: TextView = view.findViewById(R.id.tv_note)
    }

    // 4. 화면(껍데기) 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    // 5. [핵심] 데이터 끼워 넣기 (Binding)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        // 날짜랑 비고 넣기
        holder.tvDate.text = item.date
        holder.tvNote.text = item.note

        // 수입/지출에 따라 글자랑 색깔 바꾸기
        if (item.type == "수입") {
            holder.tvType.text = "수입"
            holder.tvType.setTextColor(Color.BLUE)
            // 금액 앞에 + 붙이고 콤마 넣기
            holder.tvAmount.text = "+${decimalFormat.format(item.amount)}원"
            holder.tvAmount.setTextColor(Color.BLUE)
        } else {
            holder.tvType.text = "지출"
            holder.tvType.setTextColor(Color.RED)
            // 금액 앞에 - 붙이고 콤마 넣기
            holder.tvAmount.text = "-${decimalFormat.format(item.amount)}원"
            holder.tvAmount.setTextColor(Color.RED)
        }

        // 꾹 누르면 삭제 기능
        holder.itemView.setOnLongClickListener {
            onDeleteClick(item)
            true
        }
    }

    // 6. 개수 세기
    override fun getItemCount() = list.size

    // 7. 데이터 갱신 함수 (HomeFragment에서 호출됨)
    fun setData(newList: List<TransactionEntity>) {
        list = newList
        notifyDataSetChanged() // "데이터 바뀌었으니 새로 그려라!" 명령
    }
}