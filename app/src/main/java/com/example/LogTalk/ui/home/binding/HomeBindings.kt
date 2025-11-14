// DataBinding/확장함수 묶음
package com.example.logtalk.ui.home.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.logtalk.ui.home.adapter.HomeAdapter
import com.example.logtalk.ui.home.adapter.item.HomeItem
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs

object HomeBindings {

    // 리스트 바인딩
    @JvmStatic
    @BindingAdapter("homeItems")
    fun RecyclerView.setHomeItems(items: List<HomeItem>?) {
        (adapter as? HomeAdapter)?.submitList(items ?: emptyList())
    }

    // 상대 시간 표시 ("54분 전", "1일 전")
    @JvmStatic
    fun formatRelativeTime(instant: Instant?): String {
        if (instant == null) return ""
        val now = ZonedDateTime.now()
        val t = instant.atZone(ZoneId.systemDefault())
        val minutes = abs(ChronoUnit.MINUTES.between(t, now))
        val hours = abs(ChronoUnit.HOURS.between(t, now))
        val days = abs(ChronoUnit.DAYS.between(t, now))
        return when {
            minutes < 60 -> "${minutes}분 전"
            hours < 24 -> "${hours}시간 전"
            else -> "${days}일 전"
        }
    }

    // TextView에 상대 시간 바인딩
    @JvmStatic
    @BindingAdapter("timeAgo")
    fun TextView.bindTimeAgo(instant: Instant?) {
        text = formatRelativeTime(instant)
    }
}
