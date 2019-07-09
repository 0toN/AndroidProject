package com.xwm.base.util

import android.Manifest.permission.EXPAND_STATUS_BAR
import android.annotation.ColorInt
import android.annotation.NonNull
import android.annotation.RequiresPermission
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.provider.Settings
import android.util.TypedValue
import android.view.*
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import com.xwm.base.R

object BarUtil {

    /**
     * 检测MIUI等国产操作系统是否开启全面屏手势操作
     *
     * @param context
     * @return
     */
    fun isComprehensiveScreenMode(context: Context): Boolean {
        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            result = Settings.Global.getInt(context.contentResolver, "force_fsg_nav_bar", 0)
        }
        return result != 0
    }

    ///////////////////////////////////////////////////////////////////////////
    // status bar
    ///////////////////////////////////////////////////////////////////////////

    private val TAG_STATUS_BAR = "TAG_STATUS_BAR"
    private val TAG_OFFSET = "TAG_OFFSET"
    private val KEY_OFFSET = -123

    /**
     * Return the status bar's height.
     *
     * @return the status bar's height
     */
    fun getStatusBarHeight(): Int {
        val resources = Resources.getSystem()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * Set the status bar's visibility.
     *
     * @param activity  The activity.
     * @param isVisible True to set status bar visible, false otherwise.
     */
    fun setStatusBarVisibility(@NonNull activity: Activity,
                               isVisible: Boolean) {
        setStatusBarVisibility(activity.window, isVisible)
    }

    /**
     * Set the status bar's visibility.
     *
     * @param window    The window.
     * @param isVisible True to set status bar visible, false otherwise.
     */
    fun setStatusBarVisibility(@NonNull window: Window,
                               isVisible: Boolean) {
        if (isVisible) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            showStatusBarView(window)
            addMarginTopEqualStatusBarHeight(window)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            hideStatusBarView(window)
            subtractMarginTopEqualStatusBarHeight(window)
        }
    }

    /**
     * Return whether the status bar is visible.
     *
     * @param activity The activity.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isStatusBarVisible(@NonNull activity: Activity): Boolean {
        val flags = activity.window.attributes.flags
        return flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == 0
    }

    /**
     * Set the status bar's light mode.
     *
     * @param activity    The activity.
     * @param isLightMode True to set status bar light mode, false otherwise.
     */
    fun setStatusBarLightMode(@NonNull activity: Activity,
                              isLightMode: Boolean) {
        setStatusBarLightMode(activity.window, isLightMode)
    }

    /**
     * Set the status bar's light mode.
     *
     * @param window      The window.
     * @param isLightMode True to set status bar light mode, false otherwise.
     */
    fun setStatusBarLightMode(@NonNull window: Window,
                              isLightMode: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            if (decorView != null) {
                var vis = decorView.systemUiVisibility
                if (isLightMode) {
                    vis = vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    vis = vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
                decorView.systemUiVisibility = vis
            }
        }
    }

    /**
     * Is the status bar light mode.
     *
     * @param activity The activity.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isStatusBarLightMode(@NonNull activity: Activity): Boolean {
        return isStatusBarLightMode(activity.window)
    }

    /**
     * Is the status bar light mode.
     *
     * @param window The window.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isStatusBarLightMode(@NonNull window: Window): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            if (decorView != null) {
                val vis = decorView.systemUiVisibility
                return vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR !== 0
            }
        }
        return false
    }

    /**
     * Add the top margin size equals status bar's height for view.
     *
     * @param view The view.
     */
    fun addMarginTopEqualStatusBarHeight(@NonNull view: View) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        view.tag = TAG_OFFSET
        val haveSetOffset = view.getTag(KEY_OFFSET)
        if (haveSetOffset != null && haveSetOffset as Boolean) return
        val layoutParams = view.layoutParams as MarginLayoutParams
        layoutParams.setMargins(layoutParams.leftMargin,
                layoutParams.topMargin + getStatusBarHeight(),
                layoutParams.rightMargin,
                layoutParams.bottomMargin)
        view.setTag(KEY_OFFSET, true)
    }

    fun setStatusBarView(view: View) {
        var bgColor = Color.TRANSPARENT
        if (view.background != null) {
            val colorDrawable = view.background as ColorDrawable
            bgColor = colorDrawable.color
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && bgColor != Color.TRANSPARENT) {
            view.setBackgroundColor(Utils.app.resources.getColor(R.color.colorPrimary))
        } else {
            view.setBackgroundColor(bgColor)
        }
        val statusBarParams = view.layoutParams
        view.post {
            var hasCutpout = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val displayCutout = view.rootWindowInsets.displayCutout
                if (displayCutout != null) {
                    statusBarParams.height = displayCutout.safeInsetTop
                    hasCutpout = true
                }
            }
            if (!hasCutpout) {
                statusBarParams.height = getStatusBarHeight()
            }
            view.layoutParams = statusBarParams
        }
    }


    /**
     * Subtract the top margin size equals status bar's height for view.
     *
     * @param view The view.
     */
    fun subtractMarginTopEqualStatusBarHeight(@NonNull view: View) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        val haveSetOffset = view.getTag(KEY_OFFSET)
        if (haveSetOffset == null || !(haveSetOffset as Boolean)) return
        val layoutParams = view.layoutParams as MarginLayoutParams
        layoutParams.setMargins(layoutParams.leftMargin,
                layoutParams.topMargin - getStatusBarHeight(),
                layoutParams.rightMargin,
                layoutParams.bottomMargin)
        view.setTag(KEY_OFFSET, false)
    }

    private fun addMarginTopEqualStatusBarHeight(window: Window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        val withTag: View = window.decorView.findViewWithTag(TAG_OFFSET) ?: return
        addMarginTopEqualStatusBarHeight(withTag)
    }

    private fun subtractMarginTopEqualStatusBarHeight(window: Window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        val withTag: View = window.decorView.findViewWithTag(TAG_OFFSET) ?: return
        subtractMarginTopEqualStatusBarHeight(withTag)
    }

    /**
     * Set the status bar's color.
     *
     * @param activity The activity.
     * @param color    The status bar's color.
     */
    fun setStatusBarColor(@NonNull activity: Activity,
                          @ColorInt color: Int): View? {
        return setStatusBarColor(activity, color, false)
    }

    /**
     * Set the status bar's color.
     *
     * @param activity The activity.
     * @param color    The status bar's color.
     * @param isDecor  True to add fake status bar in DecorView,
     * false to add fake status bar in ContentView.
     */
    fun setStatusBarColor(@NonNull activity: Activity,
                          @ColorInt color: Int,
                          isDecor: Boolean): View? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return null
        transparentStatusBar(activity)
        return applyStatusBarColor(activity, color, isDecor)
    }

    /**
     * Set the status bar's color.
     *
     * @param fakeStatusBar The fake status bar view.
     * @param color         The status bar's color.
     */
    fun setStatusBarColor(@NonNull fakeStatusBar: View,
                          @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        val activity = getActivityByView(fakeStatusBar) ?: return
        transparentStatusBar(activity)
        fakeStatusBar.visibility = View.VISIBLE
        val layoutParams = fakeStatusBar.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = getStatusBarHeight()
        fakeStatusBar.setBackgroundColor(color)
    }

    /**
     * Set the custom status bar.
     *
     * @param fakeStatusBar The fake status bar view.
     */
    fun setStatusBarCustom(@NonNull fakeStatusBar: View) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        val activity = getActivityByView(fakeStatusBar) ?: return
        transparentStatusBar(activity)
        fakeStatusBar.visibility = View.VISIBLE
        var layoutParams = fakeStatusBar.layoutParams
        if (layoutParams == null) {
            layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight()
            )
            fakeStatusBar.layoutParams = layoutParams
        } else {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = getStatusBarHeight()
        }
    }

    /**
     * Set the status bar's color for DrawerLayout.
     *
     * DrawLayout must add `android:fitsSystemWindows="true"`
     *
     * @param drawer        The DrawLayout.
     * @param fakeStatusBar The fake status bar view.
     * @param color         The status bar's color.
     */
    fun setStatusBarColor4Drawer(@NonNull drawer: DrawerLayout,
                                 @NonNull fakeStatusBar: View,
                                 @ColorInt color: Int) {
        setStatusBarColor4Drawer(drawer, fakeStatusBar, color, false)
    }

    /**
     * Set the status bar's color for DrawerLayout.
     *
     * DrawLayout must add `android:fitsSystemWindows="true"`
     *
     * @param drawer        The DrawLayout.
     * @param fakeStatusBar The fake status bar view.
     * @param color         The status bar's color.
     * @param isTop         True to set DrawerLayout at the top layer, false otherwise.
     */
    fun setStatusBarColor4Drawer(@NonNull drawer: DrawerLayout,
                                 @NonNull fakeStatusBar: View,
                                 @ColorInt color: Int,
                                 isTop: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        val activity = getActivityByView(fakeStatusBar) ?: return
        transparentStatusBar(activity)
        drawer.fitsSystemWindows = false
        setStatusBarColor(fakeStatusBar, color)
        var i = 0
        val count = drawer.childCount
        while (i < count) {
            drawer.getChildAt(i).fitsSystemWindows = false
            i++
        }
        if (isTop) {
            hideStatusBarView(activity)
        } else {
            setStatusBarColor(activity, color, false)
        }
    }

    private fun applyStatusBarColor(activity: Activity,
                                    color: Int,
                                    isDecor: Boolean): View? {
        val parent = if (isDecor)
            activity.window.decorView as ViewGroup
        else
            activity.findViewById<View>(android.R.id.content) as ViewGroup
        var fakeStatusBarView: View? = parent.findViewWithTag(TAG_STATUS_BAR)
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.visibility === View.GONE) {
                fakeStatusBarView.visibility = View.VISIBLE
            }
            fakeStatusBarView.setBackgroundColor(color)
        } else {
            fakeStatusBarView = createStatusBarView(activity, color)
            parent.addView(fakeStatusBarView)
        }
        return fakeStatusBarView
    }

    private fun hideStatusBarView(activity: Activity) {
        hideStatusBarView(activity.window)
    }

    private fun hideStatusBarView(window: Window) {
        val decorView = window.decorView as ViewGroup
        val fakeStatusBarView = decorView.findViewWithTag<View>(TAG_STATUS_BAR) ?: return
        fakeStatusBarView.visibility = View.GONE
    }

    private fun showStatusBarView(window: Window) {
        val decorView = window.decorView as ViewGroup
        val fakeStatusBarView = decorView.findViewWithTag<View>(TAG_STATUS_BAR) ?: return
        fakeStatusBarView.visibility = View.VISIBLE
    }

    private fun createStatusBarView(activity: Activity,
                                    color: Int): View {
        val statusBarView = View(activity)
        statusBarView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight())
        statusBarView.setBackgroundColor(color)
        statusBarView.tag = TAG_STATUS_BAR
        return statusBarView
    }

    private fun transparentStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        val window = activity.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val vis = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.decorView.systemUiVisibility = option or vis
            } else {
                window.decorView.systemUiVisibility = option
            }
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

///////////////////////////////////////////////////////////////////////////
// action bar
///////////////////////////////////////////////////////////////////////////

    /**
     * Return the action bar's height.
     *
     * @return the action bar's height
     */
    fun getActionBarHeight(): Int {
        val tv = TypedValue()
        return if (Utils.app.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(
                    tv.data, Utils.app.resources.displayMetrics
            )
        } else 0
    }

///////////////////////////////////////////////////////////////////////////
// notification bar
///////////////////////////////////////////////////////////////////////////

    /**
     * Set the notification bar's visibility.
     *
     * Must hold `<uses-permission android:name="android.permission.EXPAND_STATUS_BAR" />`
     *
     * @param isVisible True to set notification bar visible, false otherwise.
     */
    @RequiresPermission(EXPAND_STATUS_BAR)
    fun setNotificationBarVisibility(isVisible: Boolean) {
        val methodName: String
        if (isVisible) {
            methodName = if (Build.VERSION.SDK_INT <= 16) "expand" else "expandNotificationsPanel"
        } else {
            methodName = if (Build.VERSION.SDK_INT <= 16) "collapse" else "collapsePanels"
        }
        invokePanels(methodName)
    }

    private fun invokePanels(methodName: String) {
        try {
            @SuppressLint("WrongConstant")
            val service = Utils.app.getSystemService("statusbar")
            @SuppressLint("PrivateApi")
            val statusBarManager = Class.forName("android.app.StatusBarManager")
            val expand = statusBarManager.getMethod(methodName)
            expand.invoke(service)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

///////////////////////////////////////////////////////////////////////////
// navigation bar
///////////////////////////////////////////////////////////////////////////

    /**
     * Return the navigation bar's height.
     *
     * @return the navigation bar's height
     */
    fun getNavBarHeight(): Int {
        val res = Resources.getSystem()
        val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId != 0) {
            res.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /**
     * Set the navigation bar's visibility.
     *
     * @param activity  The activity.
     * @param isVisible True to set navigation bar visible, false otherwise.
     */
    fun setNavBarVisibility(@NonNull activity: Activity, isVisible: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        setNavBarVisibility(activity.window, isVisible)

    }

    /**
     * Set the navigation bar's visibility.
     *
     * @param window    The window.
     * @param isVisible True to set navigation bar visible, false otherwise.
     */
    fun setNavBarVisibility(@NonNull window: Window, isVisible: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        val decorView = window.decorView as ViewGroup
        var i = 0
        val count = decorView.childCount
        while (i < count) {
            val child = decorView.getChildAt(i)
            val id = child.id
            if (id != View.NO_ID) {
                val resourceEntryName = Utils.app
                        .resources
                        .getResourceEntryName(id)
                if ("navigationBarBackground" == resourceEntryName) {
                    child.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                }
            }
            i++
        }
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        if (isVisible) {
            decorView.systemUiVisibility = decorView.systemUiVisibility and uiOptions.inv()
        } else {
            decorView.systemUiVisibility = decorView.systemUiVisibility or uiOptions
        }
    }

    /**
     * Return whether the navigation bar visible.
     *
     * Call it in onWindowFocusChanged will get right result.
     *
     * @param activity The activity.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isNavBarVisible(@NonNull activity: Activity): Boolean {
        return isNavBarVisible(activity.window)
    }

    /**
     * Return whether the navigation bar visible.
     *
     * Call it in onWindowFocusChanged will get right result.
     *
     * @param window The window.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isNavBarVisible(@NonNull window: Window): Boolean {
        var isVisible = false
        val decorView = window.decorView as ViewGroup
        var i = 0
        val count = decorView.childCount
        while (i < count) {
            val child = decorView.getChildAt(i)
            val id = child.id
            if (id != View.NO_ID) {
                val resourceEntryName = Utils.app
                        .resources
                        .getResourceEntryName(id)
                if ("navigationBarBackground" == resourceEntryName && child.visibility === View.VISIBLE) {
                    isVisible = true
                    break
                }
            }
            i++
        }
        if (isVisible) {
            val visibility = decorView.systemUiVisibility
            isVisible = visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION === 0
        }
        return isVisible
    }

    /**
     * Set the navigation bar's color.
     *
     * @param activity The activity.
     * @param color    The navigation bar's color.
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setNavBarColor(@NonNull activity: Activity, @ColorInt color: Int) {
        setNavBarColor(activity.window, color)
    }

    /**
     * Set the navigation bar's color.
     *
     * @param window The window.
     * @param color  The navigation bar's color.
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setNavBarColor(@NonNull window: Window, @ColorInt color: Int) {
        window.navigationBarColor = color
    }

    /**
     * Return the color of navigation bar.
     *
     * @param activity The activity.
     * @return the color of navigation bar
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getNavBarColor(@NonNull activity: Activity): Int {
        return getNavBarColor(activity.window)
    }

    /**
     * Return the color of navigation bar.
     *
     * @param window The window.
     * @return the color of navigation bar
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getNavBarColor(@NonNull window: Window): Int {
        return window.navigationBarColor
    }

    /**
     * Return whether the navigation bar visible.
     *
     * @return `true`: yes<br></br>`false`: no
     */
    fun isSupportNavBar(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val wm = Utils.app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            val realSize = Point()
            display.getSize(size)
            display.getRealSize(realSize)
            return realSize.y != size.y || realSize.x != size.x
        }
        val menu = ViewConfiguration.get(Utils.app).hasPermanentMenuKey()
        val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        return !menu && !back
    }

    private fun getActivityByView(@NonNull view: View): Activity? {
        var context = view.context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        LogUtil.e("BarUtils", "the view's Context is not an Activity.")
        return null
    }
}