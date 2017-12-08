package ru.SnowVolf.girl.compat

import android.os.Build
import android.text.Spanned

/**
 * Created by Snow Volf on 28.09.2017, 20:37
 *
 * Class that brings compat with
 * {@link android.text.Html.fromHtml}
 * for an older android versions
 */

object Html {

    fun htmlCompat(htmlString: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            android.text.Html.fromHtml(htmlString, android.text.Html.FROM_HTML_MODE_LEGACY)
        else
            android.text.Html.fromHtml(htmlString)
    }

}
