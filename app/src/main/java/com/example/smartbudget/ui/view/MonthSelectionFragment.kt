package com.example.smartbudget.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartbudget.databinding.FragmentMonthSelectionBinding
import java.util.Calendar

class MonthSelectionFragment : Fragment() {

    private var _binding: FragmentMonthSelectionBinding? = null
    private val binding get() = _binding!!

    // 1. 현재 연도를 저장할 변수 (앱 실행 시점의 연도로 초기화)
    private var currentYear = Calendar.getInstance().get(Calendar.YEAR)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 화면 켜지자마자 현재 연도("2025") 표시
        updateYearText()

        // 2. 왼쪽 화살표 (<) 클릭 시
        binding.btnPrevYear.setOnClickListener {
            currentYear-- // 연도 1 감소
            updateYearText() // 화면 갱신
        }

        // 3. 오른쪽 화살표 (>) 클릭 시
        binding.btnNextYear.setOnClickListener {
            currentYear++ // 연도 1 증가
            updateYearText() // 화면 갱신
        }

        // 4. 1월~12월 버튼 클릭 설정 (반복문 사용)
        setupMonthButtons()
    }

    // 연도 텍스트뷰에 현재 연도를 반영하는 함수
    private fun updateYearText() {
        binding.tvYear.text = currentYear.toString()
    }

    // 12개의 월 버튼에 클릭 리스너를 한 번에 다는 함수
    private fun setupMonthButtons() {
        // 리스트에 버튼들을 순서대로 담습니다.
        val monthButtons = listOf(
            binding.btnJan, binding.btnFeb, binding.btnMar, binding.btnApr,
            binding.btnMay, binding.btnJun, binding.btnJul, binding.btnAug,
            binding.btnSep, binding.btnOct, binding.btnNov, binding.btnDec
        )

        // 반복문을 돌면서 리스너 연결
        monthButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                // index는 0부터 시작하므로 +1을 해줍니다. (0 -> 1월)
                // String.format("%02d")는 숫자를 두 자리 문자로 만듭니다. (1 -> "01", 10 -> "10")
                val monthStr = String.format("%02d", index + 1)

                // 최종 문자열 조합: "2025-01"
                val selectedDate = "$currentYear-$monthStr"

                // 화면 이동 및 데이터 전달
                val action = MonthSelectionFragmentDirections
                    .actionMonthSelectionFragmentToStatisticsFragment(selectedMonth = selectedDate)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}