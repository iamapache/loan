package com.bjxapp.online.base.etx.util

import android.annotation.SuppressLint
import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bjxapp.online.base.App
import com.bjxapp.online.util.SettingUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lemon.now.online.R



//绑定普通的Recyclerview
fun RecyclerView.init(
    layoutManger: RecyclerView.LayoutManager,
    bindAdapter: RecyclerView.Adapter<*>,
    isScroll: Boolean = true
): RecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}



fun RecyclerView.initFloatBtn(floatbtn: FloatingActionButton) {
    //监听recyclerview滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        @SuppressLint("RestrictedApi")
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!canScrollVertically(-1)) {
                floatbtn.visibility = View.INVISIBLE
            }
        }
    })
    floatbtn.backgroundTintList = SettingUtil.getOneColorStateList(App.instance)
    floatbtn.setOnClickListener {
        val layoutManager = layoutManager as LinearLayoutManager
        //如果当前recyclerview 最后一个视图位置的索引大于等于40，则迅速返回顶部，否则带有滚动动画效果返回到顶部
        if (layoutManager.findLastVisibleItemPosition() >= 40) {
            scrollToPosition(0)//没有动画迅速返回到顶部(马上)
        } else {
            smoothScrollToPosition(0)//有滚动动画返回到顶部(有点慢)
        }
    }
}

//初始化 SwipeRefreshLayout
fun SwipeRefreshLayout.init(onRefreshListener: () -> Unit) {
    this.run {
        setOnRefreshListener {
            onRefreshListener.invoke()
        }
        //设置主题颜色
        setColorSchemeColors(SettingUtil.getColor(App.instance))
    }
}

/**
 * 初始化普通的toolbar 只设置标题
 */
fun Toolbar.init(titleStr: String = ""): Toolbar {
    setBackgroundColor(SettingUtil.getColor(App.instance))
    title = titleStr
    return this
}

fun Toolbar.initClose(
    titleStr: String = "",
    backImg: Int = R.mipmap.ic_back,
    onBack: (toolbar: Toolbar) -> Unit
): Toolbar {
    setBackgroundColor(SettingUtil.getColor(App.instance))
    title = titleStr.toHtml()
    setNavigationIcon(backImg)
    setNavigationOnClickListener { onBack.invoke(this)

    }
    return this
}
//设置左侧图标之后在调用
fun Toolbar.setTitleCenter(text: String, sub: String? = null) {
    //设置标题
    title = text;subtitle = sub
    var titleWidth = 0
    //完全是心理暗示 正常是标题先初始化 然后是副标题
    var subView: TextView? = null
    forEach { view ->
        if (view is ImageButton && navigationIcon == view.drawable) {
            //如果左边有图标就将titleMarginStart减少 通过打印发现左边有图标的时候titleMarginStart初始化就位10 所以这里减掉
            view.post { titleMarginStart -= view.width + 10 }
        } else if (view is TextView && text == view.text) {
            view.post {
                titleWidth = view.width
                subView?.setParams(width = titleWidth)
                //屏幕宽度的一般减去title的宽度的一般 就是 titleMarginStart
                titleMarginStart += App.instance.screenWidth / 2 - view.width / 2
            }
        } else if (view is TextView && sub == view.text) {
            //副标题内容居中
            view.gravity = Gravity.CENTER
            view.post {
                //设置副标题和主标题一样宽
                if (titleWidth == 0) subView = view else view.setParams(width = titleWidth)
            }
        }
    }
}
fun View.setParams(width: Int? = null, height: Int? = null) {
    val params = layoutParams
    if (width != null) params.width = width
    if (height != null) params.height = height
    layoutParams = params
}

fun ViewPager2.init(
    fragment: Fragment,
    fragments: ArrayList<Fragment>,
    isUserInputEnabled: Boolean = true
): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = isUserInputEnabled
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int) = fragments[position]
        override fun getItemCount() = fragments.size
    }
    return this
}


/**
 * 隐藏软键盘
 */
fun hideSoftKeyboard(activity: Activity?) {
    activity?.let { act ->
        val view = act.currentFocus
        view?.let {
            val inputMethodManager =
                act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}
