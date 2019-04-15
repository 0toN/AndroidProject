package com.xwm.androidproject.util

import com.xwm.androidproject.constants.RegexConstants

import java.util.regex.Pattern

/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2016/08/02
 * desc  : utils about regex
</pre> *
 */
object RegexUtil {

    ///////////////////////////////////////////////////////////////////////////
    // If u want more please visit http://toutiao.com/i6231678548520731137
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return whether input matches regex of simple mobile.
     *
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isMobileSimple(input: CharSequence): Boolean {
        return isMatch(RegexConstants.REGEX_MOBILE_SIMPLE, input)
    }

    /**
     * Return whether input matches regex of exact mobile.
     *
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isMobileExact(input: CharSequence): Boolean {
        return isMatch(RegexConstants.REGEX_MOBILE_EXACT, input)
    }

    /**
     * Return whether input matches the regex.
     *
     * @param regex The regex.
     * @param input The input.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isMatch(regex: String, input: CharSequence?): Boolean {
        return input != null && input.length > 0 && Pattern.matches(regex, input)
    }
}
