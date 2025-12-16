// 상태/이벤트/로직
package com.example.logtalk.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.domain.usecase.CreateSessionUseCase
import com.example.logtalk.domain.usecase.ObserveSessionsUseCase
import com.example.logtalk.domain.usecase.DeleteSessionUseCase
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
    private val createSession: CreateSessionUseCase,
    private val deleteSession: DeleteSessionUseCase
) : ViewModel() {

    private val intents = MutableSharedFlow<HomeIntent>(extraBufferCapacity = 64)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    sealed interface NavEvent { //네비게이션 이벤트 누락 방지
        data class ToChat(val sessionId: Long) : NavEvent
    }
    private val _navEvents = Channel<NavEvent>(Channel.BUFFERED)
    val navEvents = _navEvents.receiveAsFlow()

    init { //ViewModel 생성 시
        observeIntents() //사용자 행동 수신 시작
        observeSessionsFlow()//세션 데이터 관찰 시작
    }

    fun sendIntent(intent: HomeIntent) {
        intents.tryEmit(intent)
    }

    //실시간으로 세션 변경사항 관찰
    private fun observeSessionsFlow() = viewModelScope.launch {
        //검색어와 세션 목록을 결합하여 필터링
        combine(
            observeSessions(),
            _searchQuery
        ) { sessions, query ->
            if (query.isBlank()) { //검색어 없으면 -> 전체
                sessions
            } else { //있으면 -> title/lastMessage 기준 필터링
                sessions.filter { session ->
                    session.title.contains(query, ignoreCase = true) ||
                            (session.lastMessage?.contains(query, ignoreCase = true) == true)
                }
            }
        }.collect { filteredSessions -> //결과 수집
            renderList(filteredSessions) //UI 상태 생성
        }
    }

    //사용자 행동 관찰
    private fun observeIntents() = viewModelScope.launch {
        intents.collect { intent ->
            when (intent) {
                is HomeIntent.SearchChanged -> {
                    _searchQuery.value = intent.query
                }
                is HomeIntent.CardClicked -> _navEvents.send(NavEvent.ToChat(intent.sessionId))
                is HomeIntent.FabClicked -> onFabClicked()
                is HomeIntent.PullToRefresh -> {
                    //Flow가 자동으로 업데이트하므로 별도 처리 불필요
                    //나중에 쓸 수 있어서 남겨둠 ㅇㅇ <- 구조적 안정성 추구
                }
                is HomeIntent.DeleteSession -> onDeleteSession(intent.sessionId)
            }
        }
    }

    private fun onFabClicked() = viewModelScope.launch {
        val newId = createSession()
        _navEvents.send(NavEvent.ToChat(newId))
    }

    private fun onDeleteSession(sessionId: Long) = viewModelScope.launch {
        deleteSession(sessionId)
        //observeSessions Flow가 자동으로 반영해줌
    }

    private fun renderList(domainList: List<com.example.logtalk.domain.model.Session>) {
        if (domainList.isEmpty()) { //빈 리스트 처리(검색 결과 x, 초기 데이터 x)
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
