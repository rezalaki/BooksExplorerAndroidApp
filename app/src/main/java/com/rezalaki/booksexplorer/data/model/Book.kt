package com.rezalaki.booksexplorer.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


@Parcelize
@Keep
data class Book(
    val id: Int,
    val title: String,
    val authorName: String,
    val firstPublishYear: Int,
    val coverId: Int,
    val imageUrl: String

) : Parcelable
