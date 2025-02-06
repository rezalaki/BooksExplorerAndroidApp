package com.rezalaki.booksexplorer.ui.base

import androidx.fragment.app.Fragment
import com.rezalaki.booksexplorer.R
import dagger.hilt.android.AndroidEntryPoint
import org.aviran.cookiebar2.CookieBar
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    fun showNetworkErrorMessage() {
        showErrorMessage(resources.getString(R.string.network_not_available))
    }

    fun showErrorMessage(message: String) {
        CookieBar.build(requireActivity())
            .setBackgroundColor(R.color.red)
            .setMessageColor(R.color.white)
            .setDuration(3_500)
            .setMessage(message)
            .show()
    }

}