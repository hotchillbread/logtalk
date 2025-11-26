// sealed class (SessionItem/EmptyItem/HeaderItem)
package com.example.logtalk.ui.home.adapter.item

import java.time.Instant

sealed class HomeItem {
    data class SessionItem(
        val id: Long,
        val title: String,
        val lastMessage: String?,
        val updatedAt: Instant
    ) : HomeItem()

    data object HeaderItem : HomeItem()
    data object EmptyItem : HomeItem()
}
