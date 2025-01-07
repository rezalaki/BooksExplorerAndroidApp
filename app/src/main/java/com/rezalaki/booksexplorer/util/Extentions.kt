package com.rezalaki.booksexplorer.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import coil.load
import com.rezalaki.booksexplorer.BuildConfig
import com.rezalaki.booksexplorer.R


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