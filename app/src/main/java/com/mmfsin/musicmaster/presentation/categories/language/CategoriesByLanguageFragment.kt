package com.mmfsin.musicmaster.presentation.categories.language

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.mmfsin.musicmaster.base.BaseFragment
import com.mmfsin.musicmaster.databinding.FragmentCategoriesByLanguageBinding
import com.mmfsin.musicmaster.databinding.FragmentYearMultiplayerBinding
import com.mmfsin.musicmaster.domain.utils.LANGUAGE
import com.mmfsin.musicmaster.domain.utils.showErrorDialog
import com.mmfsin.musicmaster.presentation.categories.language.interfaces.ICategoryListener
import com.mmfsin.musicmaster.presentation.categories.viewpager.CategoriesEvent
import com.mmfsin.musicmaster.presentation.categories.viewpager.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesByLanguageFragment : BaseFragment<FragmentCategoriesByLanguageBinding, CategoriesViewModel>(),
    ICategoryListener {

    override val viewModel: CategoriesViewModel by viewModels()
    private lateinit var mContext: Context

    private var language: String? = null

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentCategoriesByLanguageBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        presenter.getCategories()
    }

    override fun getBundleArgs() {
        arguments?.let {
            language = it.getString(LANGUAGE)
        }
    }

    override fun setUI() {
        binding.apply {
//            loading.root.visibility = View.VISIBLE
        }
        language?.let { Toast.makeText(mContext, it, Toast.LENGTH_SHORT).show() }
    }

    override fun setListeners() {
        binding.apply { }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is CategoriesEvent.Categories -> {}
                is CategoriesEvent.SomethingWentWrong -> error()
            }
        }
    }

    override fun onCategoryClick(category: String) {
        TODO("Not yet implemented")
    }

    private fun error() = activity?.showErrorDialog()

//    override fun categoriesReady() {
//        when (language) {
//            SPANISH -> presenter.getSpanishCategories()
//            ENGLISH -> presenter.getEnglishCategories()
//        }
//    }
//
//    override fun getCategoriesInfo(info: List<Category>) {
//        binding.apply {
//            recyclerView.apply {
//                layoutManager = LinearLayoutManager(mContext)
//                adapter = CategoriesAdapter(info, this@CategoriesFragment)
//                binding.recyclerView.adapter = adapter
//            }
//            loading.root.visibility = View.GONE
//        }
//    }
//
//    override fun onCategoryClick(category: String) {
//        listener.openFragmentSelector(category)
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}