package com.example.logtalk.ui.home.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.logtalk.databinding.ItemSessionCardBinding
import com.example.logtalk.ui.home.adapter.item.HomeItem
import com.example.logtalk.ui.home.binding.HomeBindings // for extension helpers, optional

class SessionCardViewHolder(
    private val binding: ItemSessionCardBinding,
    private val onClick: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HomeItem.SessionItem) = with(binding) {
        tvTitle.text = item.title
        tvSubtitle.text = item.lastMessage ?: "메시지가 없습니다"
        tvTime.text = HomeBindings.formatRelativeTime(item.updatedAt) //helper 사용
        root.setOnClickListener { onClick(item.id) }
        divider.visibility = View.VISIBLE
    }
}
