package com.mmfsin.musicmaster.presentation.categories.language

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmfsin.musicmaster.R
import com.mmfsin.musicmaster.base.BaseFragment
import com.mmfsin.musicmaster.databinding.FragmentCategoriesByLanguageBinding
import com.mmfsin.musicmaster.domain.models.Category
import com.mmfsin.musicmaster.presentation.categories.language.adapter.CategoriesAdapter
import com.mmfsin.musicmaster.presentation.categories.language.interfaces.ICategoryListener
import com.mmfsin.musicmaster.presentation.categories.viewpager.bottomsheet.interfaces.IBSheetSelectorListener
import com.mmfsin.musicmaster.presentation.models.GameMode
import com.mmfsin.musicmaster.presentation.models.GameMode.GUESS_TITLE
import com.mmfsin.musicmaster.presentation.models.GameMode.GUESS_YEAR_MULTIPLAYER
import com.mmfsin.musicmaster.presentation.models.GameMode.GUESS_YEAR_SINGLE
import com.mmfsin.musicmaster.utils.CATEGORY_ID
import com.mmfsin.musicmaster.utils.LANGUAGE
import com.mmfsin.musicmaster.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesByLanguageFragment(private val bSheetListener: IBSheetSelectorListener?) :
    BaseFragment<FragmentCategoriesByLanguageBinding, CategoriesByLanguageViewModel>(),
    ICategoryListener {

    override val viewModel: CategoriesByLanguageViewModel by viewModels()
    private lateinit var mContext: Context

    private var language: String? = null

    override fun inflateView(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentCategoriesByLanguageBinding.inflate(inflater, container, false)

    override fun getBundleArgs() {
        arguments?.let { language = it.getString(LANGUAGE) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        language?.let { viewModel.getCategoriesByLanguage(it) } ?: run { error() }
    }

    override fun setUI() {
        binding.apply {
            loading.root.visibility = View.VISIBLE
        }
    }

    override fun observe() {
        viewModel.event.observe(this) { event ->
            when (event) {
                is CategoriesByLanguageEvent.Categories -> setUpRecyclerView(event.result)
                is CategoriesByLanguageEvent.SomethingWentWrong -> error()
                else -> {}
            }
        }
    }

    private fun setUpRecyclerView(categories: List<Category>) {
        binding.apply {
            rvCategories.apply {
                layoutManager = LinearLayoutManager(mContext)
                adapter = CategoriesAdapter(
                    categories.sortedBy { it.order }, this@CategoriesByLanguageFragment
                )
            }
            loading.root.visibility = View.GONE
        }
    }

    override fun onCategoryClick(id: String) {
        bSheetListener?.onItemClick(categoryId = id)
    }

    private fun error() = activity?.showErrorDialog()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
}