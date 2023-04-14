package com.mmfsin.musicmaster.presentation.category.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.databinding.ItemCategoryBinding
import com.mmfsin.musicmaster.domain.models.CategoryDTO
import com.mmfsin.musicmaster.presentation.category.interfaces.ICategoryListener

class RViewAdapter(private val data: List<CategoryDTO>, val listener: ICategoryListener) :
    RecyclerView.Adapter<RViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCategoryBinding.bind(view)
        fun bind(category: CategoryDTO) {
            val context = binding.root.context
            binding.apply {
                Glide.with(context).load(category.icon).into(image);
                tvTitle.text = category.title
//                binding.name.typeface = ResourcesCompat.getFont(context, category.fontFamily)
                tvDescription.text = category.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
        holder.binding.row.setOnClickListener {
            listener.onCategoryClick(data[position].id)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}