// 화면: 목록/검색/빈상태/헤더(FAB 안내 배너)
package com.example.logtalk.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logtalk.R
import com.example.logtalk.databinding.FragmentHomeBinding
import com.example.logtalk.ui.home.adapter.HomeAdapter
import com.example.logtalk.ui.home.adapter.item.HomeItem
import com.example.logtalk.ui.home.navigation.HomeNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeNavigator {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()
    private lateinit var adapter: HomeAdapter

    private var debounceJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecycler()
        setupSearch()
        setupFab()
        collectState()
        collectNav()
    }

    private fun setupRecycler() = with(binding.recycler) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = HomeAdapter(onCardClick = { id ->
            vm.sendIntent(HomeIntent.CardClicked(id))
        }).also { this@HomeFragment.adapter = it }
        // DataBinding 사용 시: binding.recycler.adapter = adapter
        this.adapter = this@HomeFragment.adapter
    }

    private fun setupSearch() = with(binding.etSearch) {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 300ms 디바운스
                debounceJob?.cancel()
                debounceJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(300)
                    vm.sendIntent(HomeIntent.SearchChanged(s?.toString().orEmpty()))
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupFab() = with(binding.fab) {
        setOnClickListener { vm.sendIntent(HomeIntent.FabClicked) }
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.uiState.collect { state ->
                when (state) {
                    is HomeUiState.Loading -> renderLoading()
                    is HomeUiState.Empty -> renderEmpty()
                    is HomeUiState.Content -> renderContent(state.items)
                    is HomeUiState.Error -> renderError(state.message)
                }
            }
        }
    }

    private fun collectNav() {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.navEvents.collect { ev ->
                when (ev) {
                    is HomeViewModel.NavEvent.ToChat -> toChat(ev.sessionId)
                }
            }
        }
    }

    private fun renderLoading() {
        binding.progress.visibility = View.VISIBLE
        binding.emptyGroup.visibility = View.GONE
        binding.recycler.visibility = View.GONE
    }

    private fun renderEmpty() {
        binding.progress.visibility = View.GONE
        binding.emptyGroup.visibility = View.VISIBLE
        binding.recycler.visibility = View.GONE
        // 빈 리스트 + 헤더를 보여주고 싶다면 아래처럼 대체 가능
        // adapter.submitList(listOf(HomeItem.HeaderItem, HomeItem.EmptyItem))
    }

    private fun renderContent(items: List<HomeItem>) {
        binding.progress.visibility = View.GONE
        binding.emptyGroup.visibility = View.GONE
        binding.recycler.visibility = View.VISIBLE
        adapter.submitList(items)
    }

    private fun renderError(message: String) {
        binding.progress.visibility = View.GONE
        binding.emptyGroup.visibility = View.VISIBLE
        binding.tvEmptyTitle.text = getString(R.string.common_error_title)
        binding.tvEmptyDesc.text = message
    }

    override fun toChat(sessionId: Long) {
        // NavGraph의 액션 ID/디렉션에 맞춰 수정
        val action = HomeFragmentDirections.actionHomeToChat(sessionId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
