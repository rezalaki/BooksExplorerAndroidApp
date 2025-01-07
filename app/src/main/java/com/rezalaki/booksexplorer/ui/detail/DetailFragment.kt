package com.rezalaki.booksexplorer.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rezalaki.booksexplorer.data.model.Book
import com.rezalaki.booksexplorer.databinding.FragmentDetailBinding
import com.rezalaki.booksexplorer.util.loadImage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()
    private val bookDetail: Book by lazy {
        args.bookItem
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.text = if (bookDetail.title.length > 30) bookDetail.title.take(30) + "..." else bookDetail.title
        binding.tvAuthor.text = if (bookDetail.authorName.length > 30) bookDetail.authorName.take(30) + "..." else bookDetail.authorName
        binding.tvPublishYear.text = bookDetail.firstPublishYear.toString()

        binding.ivBanner.loadImage(bookDetail.imageUrl)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}