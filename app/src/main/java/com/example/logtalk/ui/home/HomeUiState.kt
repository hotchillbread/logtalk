// sealed interface (Loading/Empty/Content/Error)
package com.example.logtalk.ui.home

import com.example.logtalk.ui.home.adapter.item.HomeItem

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data object Empty : HomeUiState
    data class Content(val items: List<HomeItem>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
