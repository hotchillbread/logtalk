// ListAdapter + DiffUtil
package com.example.logtalk.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.logtalk.databinding.ItemEmptyStateBinding
import com.example.logtalk.databinding.ItemHomeHeaderBinding
import com.example.logtalk.databinding.ItemSessionCardBinding
import com.example.logtalk.ui.home.adapter.holder.EmptyViewHolder
import com.example.logtalk.ui.home.adapter.holder.SessionCardViewHolder
import com.example.logtalk.ui.home.adapter.item.HomeItem

class HomeAdapter(
    private val onCardClick: (Long) -> Unit
) : ListAdapter<HomeItem, RecyclerView.ViewHolder>(Diff) {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_SESSION = 1
        private const val TYPE_EMPTY = 2

        object Diff : DiffUtil.ItemCallback<HomeItem>() {
            override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean =
                when {
                    oldItem is HomeItem.SessionItem && newItem is HomeItem.SessionItem ->
                        oldItem.id == newItem.id
                    oldItem::class == newItem::class -> true
                    else -> false
                }

            override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean =
                oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is HomeItem.HeaderItem -> TYPE_HEADER
        is HomeItem.SessionItem -> TYPE_SESSION
        is HomeItem.EmptyItem -> TYPE_EMPTY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inf = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(
                ItemHomeHeaderBinding.inflate(inf, parent, false)
            )
            TYPE_SESSION -> SessionCardViewHolder(
                ItemSessionCardBinding.inflate(inf, parent, false),
                onCardClick
            )
            else -> EmptyViewHolder(
                ItemEmptyStateBinding.inflate(inf, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is HomeItem.HeaderItem -> (holder as HeaderViewHolder).bind()
            is HomeItem.SessionItem -> (holder as SessionCardViewHolder).bind(item)
            is HomeItem.EmptyItem -> (holder as EmptyViewHolder).bind()
        }
    }

    private class HeaderViewHolder(
        private val binding: ItemHomeHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            // 배너(“상담을 시작해볼까요?”)의 정적 문구/애니메이션 등 바인딩
        }
    }
}
