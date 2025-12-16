// 상태/이벤트/로직
package com.example.logtalk.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.domain.usecase.CreateSessionUseCase
import com.example.logtalk.domain.usecase.ObserveSessionsUseCase
import com.example.logtalk.ui.home.adapter.item.HomeItem.HeaderItem
import com.example.logtalk.ui.home.adapter.item.HomeItem.SessionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeSessions: ObserveSessionsUseCase,
    private val createSession: CreateSessionUseCase
) : ViewModel() {

    private val intents = MutableSharedFlow<HomeIntent>(extraBufferCapacity = 64)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    sealed interface NavEvent {
        data class ToChat(val sessionId: Long) : NavEvent
    }
    private val _navEvents = Channel<NavEvent>(Channel.BUFFERED)
    val navEvents = _navEvents.receiveAsFlow()

    init {
        observeIntents()
        observeSessionsFlow()
    }

    fun sendIntent(intent: HomeIntent) {
        intents.tryEmit(intent)
    }

    // 실시간으로 세션 변경사항 관찰
    private fun observeSessionsFlow() = viewModelScope.launch {
        // 검색어와 세션 목록을 결합하여 필터링
        combine(
            observeSessions(),
            _searchQuery
        ) { sessions, query ->
            if (query.isBlank()) {
                sessions
            } else {
                sessions.filter { session ->
                    session.title.contains(query, ignoreCase = true) ||
                            (session.lastMessage?.contains(query, ignoreCase = true) == true)
                }
            }
        }.collect { filteredSessions ->
            renderList(filteredSessions)
        }
    }

    private fun observeIntents() = viewModelScope.launch {
        intents.collect { intent ->
            when (intent) {
                is HomeIntent.SearchChanged -> {
                    _searchQuery.value = intent.query
                }
                is HomeIntent.CardClicked -> _navEvents.send(NavEvent.ToChat(intent.sessionId))
                is HomeIntent.FabClicked -> onFabClicked()
                is HomeIntent.PullToRefresh -> {
                    // Flow가 자동으로 업데이트하므로 별도 처리 불필요
                }
            }
        }
    }

    private fun onFabClicked() = viewModelScope.launch {
        val newId = createSession()
        _navEvents.send(NavEvent.ToChat(newId))
    }

    private fun renderList(domainList: List<com.example.logtalk.domain.model.Session>) {
        if (domainList.isEmpty()) {
            // 검색어가 있는 상태에서 결과가 없으면 Empty 상태
            // 검색어가 없는 상태에서 결과가 없으면 초기 Empty 상태
            _uiState.value = HomeUiState.Empty
            return
        }
        val items = buildList {
            add(HeaderItem)
            domainList.forEach { s ->
                add(
                    SessionItem(
                        id = s.id,
                        title = s.title,
                        lastMessage = s.lastMessage,
                        updatedAt = s.updatedAt ?: s.createdAt
                    )
                )
            }
        }
        _uiState.value = HomeUiState.Content(items)
    }
}
