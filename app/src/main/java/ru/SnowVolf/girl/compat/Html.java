package ru.SnowVolf.girl.compat;

import android.os.Build;
import android.text.Spanned;

/**
 * Created by Snow Volf on 28.09.2017, 20:37
 */

public class Html {

    public static Spanned htmlCompat(String htmlString){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return android.text.Html.fromHtml(htmlString, android.text.Html.FROM_HTML_MODE_LEGACY);
        else return android.text.Html.fromHtml(htmlString);
    }

}
