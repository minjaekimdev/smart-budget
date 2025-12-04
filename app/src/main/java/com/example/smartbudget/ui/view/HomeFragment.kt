package com.example.smartbudget.ui.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartbudget.R
import com.example.smartbudget.databinding.FragmentHomeBinding
import java.text.DecimalFormat

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // 숫자를 "1,000,000" 형식으로 바꿔주는 포맷터
    private val decimalFormat = DecimalFormat("#,###")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 화면이 켜지면 저장된 예산을 불러와서 표시하기
        updateBudgetUI()

        // 2. [목표 예산 설정] 영역 클릭 시 팝업 띄우기
        binding.layoutBudgetSetting.setOnClickListener {
            showBudgetDialog()
        }

        // 3. [+] 버튼 클릭 (내역 추가 화면 이동)
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addTransactionFragment)
        }

        // 4. [월별 통계] 버튼 클릭 (월 선택 화면 이동)
        binding.btnGoMonthly.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_monthSelectionFragment)
        }
    }

    // 예산 입력 팝업 띄우기
    private fun showBudgetDialog() {
        // 1. 입력창(EditText) 만들기
        val editText = EditText(requireContext())
        editText.inputType = InputType.TYPE_CLASS_NUMBER
        editText.hint = "금액을 입력하세요"

        // 2. [수정됨] 여백을 주기 위한 컨테이너(FrameLayout) 만들기
        val container = android.widget.FrameLayout(requireContext())
        val params = android.widget.FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        // 좌우 여백 설정 (24dp 정도가 제목과 라인이 잘 맞습니다)
        val margin = (24 * resources.displayMetrics.density).toInt()
        params.leftMargin = margin
        params.rightMargin = margin
        editText.layoutParams = params

        // 3. 컨테이너에 입력창 넣기
        container.addView(editText)

        // 4. 다이얼로그 생성
        AlertDialog.Builder(requireContext())
            .setTitle("목표 예산 설정")
            .setMessage("이번 달 목표 지출 금액을 입력해주세요.")
            .setView(container) // [수정됨] editText 대신 container를 넣음
            .setPositiveButton("저장") { _, _ ->
                val input = editText.text.toString()
                if (input.isNotEmpty()) {
                    val amount = input.toInt()
                    saveBudget(amount) // 저장 함수 호출
                    updateBudgetUI()   // 화면 갱신
                    Toast.makeText(context, "목표 예산이 설정되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    // 예산 저장하기 : sharedPreferences 사용하여 앱을 꺼도 유지
    // RAM 메모리가 아니라 핸드폰 내부의 파일(XML)로 써서 저장한다.
    // requireActivity: 현재 Fragment가 붙어있는 Activity 객체를 가져오는 기능
    private fun saveBudget(amount: Int) {
        val sharedPref = requireActivity().getSharedPreferences("SmartBudgetPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("KEY_BUDGET", amount)
            apply() // 저장 실행
        }
    }

    // 저장된 예산 상한값 보여주기
    private fun updateBudgetUI() {
        val sharedPref = requireActivity().getSharedPreferences("SmartBudgetPrefs", Context.MODE_PRIVATE)
        val savedBudget = sharedPref.getInt("KEY_BUDGET", 0) // 값이 없으면 0 리턴

        if (savedBudget > 0) {
            // 값이 있으면: "₩ 500,000" 형태로 표시
            binding.tvBudgetLimit.text = "₩ ${decimalFormat.format(savedBudget)}"
            binding.tvBudgetLimit.setTextColor(resources.getColor(android.R.color.black, null))
        } else {
            // 값이 없으면: "설정하기 >" 표시
            binding.tvBudgetLimit.text = "설정하기 >"
            binding.tvBudgetLimit.setTextColor(resources.getColor(android.R.color.holo_orange_dark, null))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}