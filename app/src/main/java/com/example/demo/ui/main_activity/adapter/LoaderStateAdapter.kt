package com.example.demo.ui.main_activity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.databinding.ItemStateLoaderBinding

class LoaderStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoaderStateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding = ItemStateLoaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class ViewHolder(private val binding: ItemStateLoaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.apply {
                when(loadState) {
                    is LoadState.Loading -> {
                        btnRetry.visibility = View.GONE
                        progressIndicator.visibility = View.VISIBLE
                    }
                    is LoadState.Error -> {
                        progressIndicator.visibility = View.GONE
                        btnRetry.visibility = View.VISIBLE
                    }
                    else -> {
                        progressIndicator.visibility = View.GONE
                        btnRetry.visibility = View.GONE
                    }
                }

                btnRetry.setOnClickListener {
                    retry()
                }
            }
        }
    }
}