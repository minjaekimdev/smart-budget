package com.example.smartbudget.ui.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels // [중요] ViewModel 연결 도구
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartbudget.R
import com.example.smartbudget.data.model.TransactionEntity
import com.example.smartbudget.databinding.FragmentHomeBinding
import com.example.smartbudget.ui.adapter.TransactionAdapter
import com.example.smartbudget.ui.viewmodel.MainViewModel
import java.text.DecimalFormat

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // 1. ViewModel 연결 (DB 데이터 요청용)
    // MainViewModel 인스턴스 생성
    private val viewModel: MainViewModel by viewModels()

    // 2. 어댑터 선언 (리스트 연결용)
    private lateinit var adapter: TransactionAdapter

    // 3. 숫자 포맷 (1000 -> "1,000")
    private val decimalFormat = DecimalFormat("#,###")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 리싸이클러뷰 설정(어댑터 연결, 삭제를 위한 리스너 연결)
        setupRecyclerView()

        // DB 데이터 관찰 (데이터가 변하면 UI 자동 갱신)
        // DB에 내역이 추가/삭제될 때마다 이 안의 코드가 자동으로 실행된다.
        viewModel.allTransactions.observe(viewLifecycleOwner) { list ->
            // (A) 리스트에 데이터 집어넣기
            adapter.setData(list)

            // (B) 수입/지출/잔고 계산해서 화면 갱신
            updateBalanceUI(list)
        }

        // 3. 버튼 클릭 리스너들 (화면 이동)
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addTransactionFragment)
        }

        binding.btnGoMonthly.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_monthSelectionFragment)
        }

        // 4. 초기 잔고 설정 (잔고 텍스트 클릭 시)
        // (예산 설정은 뺐지만, 현재 잔고의 기준점이 되는 '초기 자산' 설정 기능은 유지했습니다)
        binding.tvTotalBalance.setOnClickListener {
            showInitialBalanceDialog()
        }
    }

    // 리싸이클러뷰 설정 및 삭제 기능 연결
    private fun setupRecyclerView() {
        // TransactionAdapter 인스턴스 생성
        adapter = TransactionAdapter { item ->
            showDeleteDialog(item)
        }
        binding.rvRecentTransactions.layoutManager = LinearLayoutManager(requireContext())
        // fragment_home.xml의 rv_recent_transactions에 adapter 연결
        binding.rvRecentTransactions.adapter = adapter
    }

    // --- [기능 2] 수입/지출/잔고 계산 및 UI 업데이트 ---
    private fun updateBalanceUI(list: List<TransactionEntity>) {
        // 1. 리스트에서 수입과 지출을 각각 다 더함
        val totalIncome = list.filter { it.type == "수입" }.sumOf { it.amount }
        val totalExpense = list.filter { it.type == "지출" }.sumOf { it.amount }

        // 2. 저장된 초기 잔고(시드머니) 불러오기
        val sharedPref = requireActivity().getSharedPreferences("SmartBudgetPrefs", Context.MODE_PRIVATE)
        val initialBalance = sharedPref.getInt("KEY_INITIAL_BALANCE", 0)

        // 3. 현재 잔고 계산 (초기잔고 + 수입 - 지출)
        val currentBalance = initialBalance + totalIncome - totalExpense

        // 4. 화면에 표시 (천 단위 콤마 포맷 적용)
        binding.tvTotalBalance.text = "₩ ${decimalFormat.format(currentBalance)}"
        binding.tvSummaryIncome.text = "수입: +${decimalFormat.format(totalIncome)}"
        binding.tvSummaryExpense.text = "지출: -${decimalFormat.format(totalExpense)}"
    }

    // --- [기능 3] 초기 잔고 설정 팝업 ---
    private fun showInitialBalanceDialog() {
        val editText = EditText(requireContext())
        editText.inputType = InputType.TYPE_CLASS_NUMBER

        // 여백을 위한 컨테이너
        val container = FrameLayout(requireContext())
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.leftMargin = 50; params.rightMargin = 50
        editText.layoutParams = params
        container.addView(editText)

        AlertDialog.Builder(requireContext())
            .setTitle("초기 잔고 설정")
            .setMessage("현재 가지고 있는 자산을 입력하세요.\n(계산의 기준값이 됩니다)")
            .setView(container)
            .setPositiveButton("저장") { _, _ ->
                val input = editText.text.toString()
                if (input.isNotEmpty()) {
                    // SharedPreferences에 저장
                    val sharedPref = requireActivity().getSharedPreferences("SmartBudgetPrefs", Context.MODE_PRIVATE)
                    sharedPref.edit().putInt("KEY_INITIAL_BALANCE", input.toInt()).apply()

                    // 화면 갱신을 위해 현재 데이터를 다시 한 번 계산 로직에 태움
                    viewModel.allTransactions.value?.let { updateBalanceUI(it) }
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    // 삭제 확인 팝업
    private fun showDeleteDialog(item: TransactionEntity) {
        // 사용자에게 표시할 대화상자를 만듦
        // requireContext(): 현재 Fragment가 연결되어 있는 Activity의 Context를 반환함
        AlertDialog.Builder(requireContext())
            .setTitle("내역 삭제")
            .setMessage("이 내역을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                viewModel.deleteTransaction(item) // DB에서 삭제 요청
                Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}