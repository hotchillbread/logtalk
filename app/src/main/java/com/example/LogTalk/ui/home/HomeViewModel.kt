// 상태/이벤트/로직
package com.example.logtalk.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.domain.usecase.CreateSessionUseCase
import com.example.logtalk.domain.usecase.GetRecentSessionsUseCase
import com.example.logtalk.domain.usecase.SearchSessionsUseCase
import com.example.logtalk.ui.home.adapter.item.HomeItem
import com.example.logtalk.ui.home.adapter.item.HomeItem.EmptyItem
import com.example.logtalk.ui.home.adapter.item.HomeItem.HeaderItem
import com.example.logtalk.ui.home.adapter.item.HomeItem.SessionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecentSessions: GetRecentSessionsUseCase,
    private val searchSessions: SearchSessionsUseCase,
    private val createSession: CreateSessionUseCase
) : ViewModel() {

    private val intents = MutableSharedFlow<HomeIntent>(extraBufferCapacity = 64)

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    sealed interface NavEvent {
        data class ToChat(val sessionId: Long) : NavEvent
    }
    private val _navEvents = Channel<NavEvent>(Channel.BUFFERED)
    val navEvents = _navEvents.receiveAsFlow()

    init {
        observeIntents()
        loadInitial()
    }

    fun sendIntent(intent: HomeIntent) {
        intents.tryEmit(intent)
    }

    private fun loadInitial() = viewModelScope.launch {
        _uiState.value = HomeUiState.Loading
        val sessions = getRecentSessions()
        renderList(sessions)
    }

    private fun observeIntents() = viewModelScope.launch {
        intents.collect { intent ->
            when (intent) {
                is HomeIntent.SearchChanged -> onSearchChanged(intent.query)
                is HomeIntent.CardClicked -> _navEvents.send(NavEvent.ToChat(intent.sessionId))
                is HomeIntent.FabClicked -> onFabClicked()
                is HomeIntent.PullToRefresh -> loadInitial()
            }
        }
    }

    private fun onSearchChanged(query: String) = viewModelScope.launch {
        // 필요 시 debounce는 Fragment/TextWatcher 측에서 처리
        val sessions = if (query.isBlank()) getRecentSessions() else searchSessions(query)
        renderList(sessions)
    }

    private fun onFabClicked() = viewModelScope.launch {
        val newId = createSession()
        _navEvents.send(NavEvent.ToChat(newId))
    }

    private fun renderList(domainList: List<com.example.logtalk.domain.model.Session>) {
        if (domainList.isEmpty()) {
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
                        updatedAt = s.updatedAt ?: Instant.now()
                    )
                )
            }
        }
        _uiState.value = HomeUiState.Content(items)
    }
}
