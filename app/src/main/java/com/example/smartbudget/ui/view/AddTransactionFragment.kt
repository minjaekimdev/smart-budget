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
// databinding: 뷰 바인딩(View Binding) 또는 데이터 바인딩(Data Binding) 기능을
// 활성화했을 때, 자동으로 생성되는 모든 바인딩 클래스를 모아두기 위해 자동 생성되는 패키지
import com.example.smartbudget.databinding.FragmentAddTransactionBinding
import java.util.Calendar

class AddTransactionFragment : Fragment() {
    private var _binding: FragmentAddTransactionBinding? = null // fragment_add_transaction.xml에 접근하기 위한 View Binding 객체
    private val binding get() = _binding!! // binding에 접근할 때마다 _binding!!의 결과값을 가져옴(널체크 없이 접근하기위함)

    // 프래그먼트가 자신의 뷰 계층 구조를 처음 그릴 때 이 메서드가 호출됨
    // inflater: LayoutInflater -> .xml 파일을 메모리의 실제 View 객체로 변환하는 데 사용되는 객체(Android시스템이 전달함)
    // container: ViewGroup -> 프래그먼트의 뷰가 배치될 부모 뷰(activity_main.xml의 nav_host_fragment)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 뷰 생성 단계
        // false 옵션: 뷰를 부모에 바로 붙이지는 않는다. 만들기만 한 상태
        // onViewCreated 직후에 시스템이 자동으로 붙임
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)

        // 뷰 반환
        return binding.root
    }

    // onViewCreated: Activity에는 없고 Fragment에만 존재하는 특별한 메서드
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
        // 현재 날짜를 담고 잇는 Calendar 객체 생성
        val calendar = Calendar.getInstance()

        // 생성된 Calendar 객체에서 현재 연도, 월, 일 정보를 추출하여 각각 year, month, day 변수에 저장
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // DatePickerDialog 객체 생성 및 실행
        // 첫 번째 인자 requireContext(): 다이얼로그를 표시할 컨텍스트
        // DatePickerDialog는 현재 앱의 테마를 상속받아 그에 맞춰 스타일을 결정하므로 Context를 전달한다
        // 두 번째 인자: 사용자가 달력에서 날짜를 선택 후 확인 버튼을 눌렀을 때 실행되는 콜백
        // .show() DatePickerDialog를 화면에 표시
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

        // --- 나중에 팀원 A가 DB 완성하면 아래 코드 추가하기 ---
        // viewModel.addTransaction(TransactionEntity(0, type, amount, date, note))
        // 2. 사용자에게 알림
        Toast.makeText(context, "저장되었습니다! (테스트 모드)", Toast.LENGTH_SHORT).show()

        // 3. 화면 닫고 홈으로 돌아가기 (뒤로 가기)
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // view binding 객체의 참조를 제거하여 메모리 누수를 방지
        _binding = null
    }
}
