package com.rezalaki.booksexplorer.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.SearchView
import coil.load
import com.rezalaki.booksexplorer.BuildConfig
import com.rezalaki.booksexplorer.R
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


fun SearchView.onTextChanged(): Flow<String> = callbackFlow {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean = false

        override fun onQueryTextChange(newText: String?): Boolean {
            trySend(newText.toString())
            return true
        }
    })
    awaitClose { setOnQueryTextListener(null) }
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.enterByScaleAnimation() {
    ScaleAnimation(
        0.0f,
        1.0f,
        0.0f,
        1.0f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.95f
    ).also {
        it.duration = 400
        this.startAnimation(it)
    }
}

fun ifIsDebug(action: ()->Unit){
    if (BuildConfig.DEBUG) {
        action.invoke()
    }
}


fun ImageView.loadImage(imageUrl: String) {
    this.load(imageUrl) {
        crossfade(true)
        crossfade(400)
        error(R.drawable.ic_error)
        placeholder(R.drawable.ic_placeholder)
    }
}