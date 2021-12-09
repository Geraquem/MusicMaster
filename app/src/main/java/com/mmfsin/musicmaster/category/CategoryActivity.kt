package com.mmfsin.musicmaster.category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mmfsin.musicmaster.category.adapter.RViewAdapter
import com.mmfsin.musicmaster.category.model.CategoryDTO
import com.mmfsin.musicmaster.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity(), CategoryView {

    lateinit var binding: ActivityCategoryBinding

    private val presenter = CategoryPresenter(this)

    private lateinit var adapter: RViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.setRecyclerViewsData()
    }

    private fun initRecyclerView(recyclerView: RecyclerView, data: List<CategoryDTO>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RViewAdapter(this, presenter, data)
        recyclerView.adapter = adapter
    }

    override fun completeEnglishRV(data: List<CategoryDTO>) {
        initRecyclerView(binding.englishRecycler, data)
    }

    override fun completeSpanishRV(data: List<CategoryDTO>) {
        initRecyclerView(binding.spanishRecycler, data)
    }

    override fun showFragmentSelector() {

    }

    override fun navigateToDashboard() {

    }
}