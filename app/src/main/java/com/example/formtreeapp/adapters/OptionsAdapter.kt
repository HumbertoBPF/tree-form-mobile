package com.example.formtreeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.formtreeapp.databinding.OptionItemBinding
import com.example.formtreeapp.models.TreeNode

class OptionsAdapter(
    private val context: Context,
    private val options: List<TreeNode>,
    private val onClickOption: (TreeNode) -> Unit
): Adapter<OptionsAdapter.ViewHolder>() {
    inner class ViewHolder(binding: OptionItemBinding): RecyclerView.ViewHolder(binding.root) {
        private val optionTextView = binding.optionTextView

        fun bind(option: TreeNode) {
            optionTextView.text = option.label
            itemView.setOnClickListener {
                onClickOption(option)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = OptionItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = options.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = options[position]
        holder.bind(option)
    }


}