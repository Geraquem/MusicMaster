package com.mmfsin.musicmaster.presentation.category

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfsin.musicmaster.common.BaseFragment
import com.mmfsin.musicmaster.databinding.FragmentCategoriesBinding
import com.mmfsin.musicmaster.domain.models.CategoryDTO
import com.mmfsin.musicmaster.domain.types.Languages
import com.mmfsin.musicmaster.domain.types.Languages.ENGLISH
import com.mmfsin.musicmaster.domain.types.Languages.SPANISH
import com.mmfsin.musicmaster.presentation.category.adapter.CategoriesAdapter
import com.mmfsin.musicmaster.presentation.category.interfaces.ICategoryListener
import com.mmfsin.musicmaster.presentation.selector.IFragmentSelector

class CategoriesFragment(private val listener: IFragmentSelector, private val language: Languages) :
    BaseFragment<FragmentCategoriesBinding>(), CategoriesView, ICategoryListener {

    private val presenter by lazy { CategoriesPresenter(this) }

    private lateinit var mContext: Context

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentCategoriesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getCategories()
    }

    override fun setUI() {
        binding.apply {
            loading.root.visibility = View.VISIBLE
        }
    }

    override fun categoriesReady() {
        when (language) {
            SPANISH -> presenter.getSpanishCategories()
            ENGLISH -> presenter.getEnglishCategories()
        }
    }

    override fun getCategoriesInfo(info: List<CategoryDTO>) {
        binding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = CategoriesAdapter(info, this@CategoriesFragment)
                binding.recyclerView.adapter = adapter
            }
            loading.root.visibility = View.GONE
        }
    }

    override fun onCategoryClick(category: String) {
        listener.openFragmentSelector(category)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}