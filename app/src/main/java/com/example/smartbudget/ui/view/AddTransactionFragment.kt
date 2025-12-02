package com.example.smartbudget.ui.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartbudget.R
import com.example.smartbudget.databinding.FragmentAddTransactionBinding
import java.util.Calendar

class AddTransactionFragment : Fragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 날짜 입력창 클릭 시 달력 띄우기
        binding.etDate.isFocusable = false // 키보드 안 뜨게 막음
        binding.etDate.setOnClickListener {
            showDatePicker()
        }

        // 2. 저장 버튼 클릭 시 '가짜 저장' 실행
        binding.btnSave.setOnClickListener {
            saveTransaction()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            // 월은 0부터 시작하므로 +1, 두 자리 숫자로 맞춤 (2025-07-01)
            val formattedDate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            binding.etDate.setText(formattedDate)
        }, year, month, day).show()
    }

    private fun saveTransaction() {
        // --- 입력값 가져오기 ---
        val date = binding.etDate.text.toString()
        val amountStr = binding.etAmount.text.toString()
        val note = binding.etNote.text.toString()

        // 라디오 버튼 확인 (수입 vs 지출)
        val type = if (binding.rbIncome.isChecked) "수입" else "지출"

        // --- 유효성 검사 (빈칸 막기) ---
        if (date.isEmpty()) {
            Toast.makeText(context, "날짜를 선택해주세요!", Toast.LENGTH_SHORT).show()
            return
        }
        if (amountStr.isEmpty()) {
            Toast.makeText(context, "금액을 입력해주세요!", Toast.LENGTH_SHORT).show()
            return
        }
        if (note.isEmpty()) {
            Toast.makeText(context, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toInt()

        // ==========================================================
        // DB 대신 로그로 확인하기 (Mocking)
        // ==========================================================

        // 1. 로그 출력: 하단 Logcat 탭에서 확인 가능
        Log.d("SmartBudget", ">>> 저장 시도! 유형:$type, 날짜:$date, 금액:$amount, 내용:$note")

        // 2. 사용자에게 알림
        Toast.makeText(context, "저장되었습니다! (테스트 모드)", Toast.LENGTH_SHORT).show()

        // 3. 화면 닫고 홈으로 돌아가기 (뒤로 가기)
        findNavController().popBackStack()

        // --- 나중에 팀원 A가 DB 완성하면 아래 코드로 바꾸면 됨 ---
        // viewModel.addTransaction(TransactionEntity(0, type, amount, date, note))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
