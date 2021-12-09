package com.mmfsin.musicmaster.category.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.category.CategoryPresenter
import com.mmfsin.musicmaster.category.model.CategoryDTO
import com.mmfsin.musicmaster.databinding.RowCategoryBinding

class RViewAdapter(
    private val context: Context,
    private val presenter: CategoryPresenter,
    private val data: List<CategoryDTO>
) :
    RecyclerView.Adapter<RViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RowCategoryBinding.bind(view)
        fun bind(context: Context, category: CategoryDTO) {
            binding.image.setBackgroundResource(category.image)
            binding.name.text = context.getString(category.name)
            binding.name.typeface = ResourcesCompat.getFont(context, category.fontFamily)
            binding.artists.text = context.getString(category.artists)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, data[position])
        holder.binding.row.setOnClickListener {
            presenter.navigateToSelector()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}