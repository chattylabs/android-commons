package chattylabs.android.commons;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

public class HtmlUtils {

    @SuppressWarnings("deprecation")
    public static Spanned from(String text) {
        if (text == null) return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }

    @SuppressWarnings("deprecation")
    public static String to(Spanned spanned) {
        if (spanned == null) return null;
        String pattern1 = "^(<p[^>]*>)*";
        String pattern2 = "(</p>)*$";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.toHtml(spanned, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
                    .replaceAll(pattern1, "")
                    .replaceAll(pattern2, "");
        } else {
            return Html.toHtml(spanned)
                       .replaceAll(pattern1, "")
                       .replaceAll(pattern2, "");
        }
    }
}
