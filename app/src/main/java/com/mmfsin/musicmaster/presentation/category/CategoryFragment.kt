package com.mmfsin.musicmaster.presentation.category

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.common.BaseFragment
import com.mmfsin.musicmaster.databinding.FragmentCategoriesBinding
import com.mmfsin.musicmaster.domain.models.CategoryDTO
import com.mmfsin.musicmaster.presentation.category.adapter.RViewAdapter
import com.mmfsin.musicmaster.presentation.selector.IFragmentSelector

class CategoryFragment(private val listener: IFragmentSelector, private val language: String) :
    BaseFragment<FragmentCategoriesBinding>(), CategoryView {

    private val presenter by lazy { CategoryPresenter(this) }

    lateinit var mContext: Context

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentCategoriesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setEnglishRVData()
        when {
            (language == getString(R.string.english)) -> presenter.setEnglishRVData()
            (language == getString(R.string.spanish)) -> presenter.setSpanishRVData()
        }
    }

    override fun setUI() {
        binding.apply {
            loading.root.visibility = View.VISIBLE
        }
    }

    override fun getCategoriesInfo(data: List<CategoryDTO>) {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = RViewAdapter(mContext, presenter, data)
            binding.recyclerView.adapter = adapter
        }
    }

    override fun showFragmentSelector(category: String) {
        listener.openFragmentSelector(category)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}