package com.mmfsin.musicmaster.category

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.category.adapter.RViewAdapter
import com.mmfsin.musicmaster.category.model.CategoryDTO
import kotlinx.android.synthetic.main.fragment_rv_category.*

class CategoryFragment(private val listener : ICategoryFragment, private val language: String) : Fragment(), CategoryView {

    private val presenter by lazy { CategoryPresenter(this) }

    lateinit var mContext: Context

    private lateinit var adapter: RViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rv_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when {
            (language == getString(R.string.english)) -> presenter.setEnglishRVData()
            (language == getString(R.string.spanish)) -> presenter.setSpanishRVData()
        }
    }

    override fun initRecyclerView(data: List<CategoryDTO>) {
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        adapter = RViewAdapter(mContext, presenter, data)
        recyclerView.adapter = adapter
    }

    override fun showFragmentSelector(id: String) {
        listener.openFragmentSelector(id)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    interface ICategoryFragment {
        fun openFragmentSelector(id: String)
    }
}