package com.rezalaki.booksexplorer.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize



@Entity
@Parcelize
@Keep
data class Book(
    @PrimaryKey val id: Int,
    val title: String,
    val authorName: String,
    val firstPublishYear: Int,
    val coverId: Int,
    val imageUrl: String

) : Parcelable
