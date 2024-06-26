package com.example.formtreeapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.formtreeapp.activities.FormTreeActivity
import com.example.formtreeapp.databinding.FormItemBinding
import com.example.formtreeapp.models.Form

class FormAdapter(
    private val context: Context,
    private val forms: List<Form>
): RecyclerView.Adapter<FormAdapter.ViewHolder>() {
    inner class ViewHolder(binding: FormItemBinding): RecyclerView.ViewHolder(binding.root) {
        private val nameTextView = binding.nameTextView
        private val descriptionTextView = binding.descriptionTextView

        fun bind(form: Form) {
            nameTextView.text = form.name
            descriptionTextView.text = form.description
            itemView.setOnClickListener {
                val intent = Intent(context, FormTreeActivity::class.java)
                intent.putExtra("form_id", form.id)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = FormItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = forms.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val form = forms[position]
        holder.bind(form)
    }
}
