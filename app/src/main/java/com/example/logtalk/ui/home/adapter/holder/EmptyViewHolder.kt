package com.example.logtalk.ui.home.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.logtalk.databinding.ItemEmptyStateBinding

class EmptyViewHolder(
    private val binding: ItemEmptyStateBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        // 정적 문구/아이콘만 표시하는 경우 바인딩 로직 없이도 OK
    }
}
