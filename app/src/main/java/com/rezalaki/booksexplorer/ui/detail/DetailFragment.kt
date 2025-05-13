package com.rezalaki.booksexplorer.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rezalaki.booksexplorer.R
import com.rezalaki.booksexplorer.data.model.Book
import com.rezalaki.booksexplorer.databinding.FragmentDetailBinding
import com.rezalaki.booksexplorer.ui.base.BaseFragment
import com.rezalaki.booksexplorer.util.enterByScaleAnimation
import com.rezalaki.booksexplorer.util.gone
import com.rezalaki.booksexplorer.util.loadImage
import com.rezalaki.booksexplorer.util.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : BaseFragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()
    private val bookDetail: Book by lazy {
        args.bookItem
    }

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.searchBook(bookDetail)

        binding.tvTitle.text =
            if (bookDetail.title.length > 30) bookDetail.title.take(30) + "..." else bookDetail.title
        binding.tvAuthor.text =
            if (bookDetail.authorName.length > 30) bookDetail.authorName.take(30) + "..." else bookDetail.authorName
        binding.tvPublishYear.text = bookDetail.firstPublishYear.toString()

        binding.ivBanner.loadImage(bookDetail.imageUrl)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DetailUiState.BookFoundFailed -> showErrorMessage(state.errorMessage)

                is DetailUiState.SaveToDbFailed -> showErrorMessage(state.errorMessage)

                is DetailUiState.DeleteFromDbFailed -> showErrorMessage(state.errorMessage)

                is DetailUiState.Loading -> binding.ivMark.gone()

                is DetailUiState.BookFoundSuccess ->  uiBookFoundSuccess(state.isBookFound)

                DetailUiState.DeleteFromDbSuccess -> uiDeleteFromDbSuccessed()

                DetailUiState.SaveToDbSuccess -> uiBookSaveToDbSuccessed()

            }
        }

    }

    private fun uiBookSaveToDbSuccessed() {
        binding.ivMark.setImageDrawable(
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_mark_fill)
        )
        binding.ivMark.visible()
        binding.ivMark.enterByScaleAnimation()
        handleMarkIconClick(true)
    }

    private fun uiDeleteFromDbSuccessed() {
        binding.ivMark.setImageDrawable(
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_mark_empty)
        )
        binding.ivMark.visible()
        binding.ivMark.enterByScaleAnimation()
        handleMarkIconClick(false)
    }

    private fun uiBookFoundSuccess(isBookFound: Boolean) {
        val drawable = if (isBookFound) {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_mark_fill)
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_mark_empty)
        }
        binding.ivMark.setImageDrawable(drawable)
        binding.ivMark.visible()
        binding.ivMark.enterByScaleAnimation()
        handleMarkIconClick(isBookFound)
    }

    private fun handleMarkIconClick(isSaved: Boolean){
        binding.ivMark.setOnClickListener {
            if (isSaved) {
                viewModel.deleteBook(bookDetail)
            }
            else {
                viewModel.saveBook(bookDetail)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}