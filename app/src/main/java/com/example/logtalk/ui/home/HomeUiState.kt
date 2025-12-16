// sealed interface (Loading/Empty/Content/Error)
package com.example.logtalk.ui.home

import com.example.logtalk.ui.home.adapter.item.HomeItem

//출력
sealed interface HomeUiState { //상태 누락 방지
    data object Loading : HomeUiState
    data object Empty : HomeUiState
    data class Content(val items: List<HomeItem>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
