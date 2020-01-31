package com.kablemonck.idea.plugins.changelistpopup.util;

/**
 * @author Kable Monck (kable.monck@fincleartech.com.au)
 */
public class TextUtil {

    public static String wrap(String text, int maxLength) {
        return wrap(text, maxLength, TextWrapStrategy.END);
    }

    public static String wrap(String text, int maxLength, TextWrapStrategy strategy) {
        if (text != null && text.length() > maxLength) {
            if (strategy == TextWrapStrategy.END) {
                return text.substring(0, maxLength - 3) + "...";
            } else {
                int lengthCut = text.length() - maxLength;
                int a = (int) Math.floor(text.length() / 2d - lengthCut / 2d);
                int b = (int) Math.floor(text.length() / 2d + lengthCut / 2d);
                return text.substring(0, a) + "..." + text.substring(b);
            }
        }
        return text;
    }

    public enum TextWrapStrategy {
        MIDDLE,
        END,
    }
}
