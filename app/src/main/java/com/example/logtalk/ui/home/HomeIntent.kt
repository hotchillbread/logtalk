// 사용자의 액션(검색 입력, 카드 클릭, FAB 클릭, 삭제)
package com.example.logtalk.ui.home

sealed interface HomeIntent { //입력
    data class SearchChanged(val query: String) : HomeIntent
    data class CardClicked(val sessionId: Long) : HomeIntent
    data object FabClicked : HomeIntent
    data object PullToRefresh : HomeIntent
    data class DeleteSession(val sessionId: Long) : HomeIntent
}
