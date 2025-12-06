package com.example.smartbudget.ui.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.smartbudget.databinding.FragmentStatisticsBinding
import com.example.smartbudget.data.model.TransactionEntity
import com.example.smartbudget.ui.adapter.TransactionAdapter
import com.example.smartbudget.ui.viewmodel.MainViewModel
import java.text.DecimalFormat

class StatisticsFragment : Fragment() {
    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    // Safe Args
    private val args: StatisticsFragmentArgs by navArgs()

    // ViewModel
    private val viewModel: MainViewModel by activityViewModels()

    // Adapter
    private lateinit var adapter: TransactionAdapter

    // Format
    private val decimalFormat = DecimalFormat("#,###")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val targetMonth = args.selectedMonth

        // 1. 제목 설정 (수정됨: tvSelectedMonth -> tvStatisticsTitle)
        setupTitle(targetMonth)

        // 2. 리싸이클러뷰 초기화 (수정됨: rvMonthlyTransactions -> rvStatisticsList)
        setupRecyclerView()

        // 3. 데이터 관찰
        viewModel.getMonthlyTransactions(targetMonth).observe(viewLifecycleOwner) { list ->
            adapter.setData(list)
            updateSummaryUI(list)

            if (list.isEmpty()) {
                Toast.makeText(context, "해당 월의 내역이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupTitle(dateStr: String) {
        val parts = dateStr.split("-")
        // [수정 포인트] XML ID가 tv_statistics_title 이므로 바인딩 변수는 tvStatisticsTitle 입니다.
        if (parts.size == 2) {
            binding.tvStatisticsTitle.text = "${parts[0]}년 ${parts[1]}월 내역"
        } else {
            binding.tvStatisticsTitle.text = "$dateStr 내역"
        }
    }

    private fun setupRecyclerView() {
        adapter = TransactionAdapter { item ->
            viewModel.deleteTransaction(item)
            Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
        }
        // [수정 포인트] XML ID가 rv_statistics_list 일 경우
        binding.rvStatisticsList.adapter = adapter
        binding.rvStatisticsList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
    }

    private fun updateSummaryUI(list: List<TransactionEntity>) {
        val totalIncome = list.filter { it.type == "수입" }.sumOf { it.amount }
        val totalExpense = list.filter { it.type == "지출" }.sumOf { it.amount }

        // (선택사항) 합계/잔고 계산이 필요하다면 사용
        val balance = totalIncome - totalExpense

        // [수정 포인트] XML ID가 tv_stat_income, tv_stat_expense 일 경우
        binding.tvStatIncome.text = "총 수입: ${decimalFormat.format(totalIncome)}"
        binding.tvStatExpense.text = "총 지출: ${decimalFormat.format(totalExpense)}"

        // 합계 텍스트뷰가 있다면 (tv_stat_total)
        binding.tvStatTotal.text = "합계: ${decimalFormat.format(balance)}원"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}