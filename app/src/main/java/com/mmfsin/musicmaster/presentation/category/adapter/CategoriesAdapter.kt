package com.mmfsin.musicmaster.presentation.category.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.databinding.ItemCategoryBinding
import com.mmfsin.musicmaster.domain.mappers.getFontFamily
import com.mmfsin.musicmaster.domain.models.CategoryDTO
import com.mmfsin.musicmaster.presentation.category.interfaces.ICategoryListener

class CategoriesAdapter(
    private val data: List<CategoryDTO>,
    private val listener: ICategoryListener
) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCategoryBinding.bind(view)
        fun bind(category: CategoryDTO) {
            val context = binding.root.context
            binding.apply {
//                val icon = getRandomIcon(category)
//                val randIcon = getRandomIcon(category)
                Glide.with(context).load(category.icon1).into(image)
                tvTitle.text = category.title
                tvTitle.typeface = ResourcesCompat.getFont(context, category.id.getFontFamily())
                tvDescription.text = category.description
            }
        }
    }

//        private fun getRandomIcon(data: CategoryDTO): String? {
//            var count = 1
//            if (data.icon2 != null) count++
//            if (data.icon3 != null) count++
//
//            val rand = (1..count).random()
//            return when (rand) {
//                2 -> data.icon2
//                3 -> data.icon3
//                else -> data.icon1
//            }
//        }
//    }

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