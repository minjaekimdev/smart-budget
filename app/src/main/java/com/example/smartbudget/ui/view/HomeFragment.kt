package com.example.smartbudget.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartbudget.R
import com.example.smartbudget.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        // 1. [+] 버튼 누르면 -> '내역 추가' 화면으로 이동
        // XML ID: fab_add -> binding.fabAdd
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addTransactionFragment)
        }

        // 2. [월별 통계 보기] 버튼 누르면 -> '월 선택' 화면으로 이동
        binding.btnGoMonthly.setOnClickListener {
            // MonthSelectionFragment로 이동하는 코드
            findNavController().navigate(R.id.action_homeFragment_to_monthSelectionFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}