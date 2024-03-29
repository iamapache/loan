package com.bjxapp.online.ui.fragment

import android.content.Intent
import android.os.Bundle
import com.bjxapp.online.base.fragment.BaseFragment
import com.bjxapp.online.base.viewmodel.BaseViewModel
import com.bjxapp.online.ui.activity.LoginActivity
import com.lemon.now.online.databinding.FragmentHomeBinding

/**
 * 项目：  Lemon Cash
 * 类名：  HomeFragment.java
 * 描述：
 */
class HomeFragment : BaseFragment<BaseViewModel, FragmentHomeBinding>() {
    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.login.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    LoginActivity::class.java
                )
            )
        }
    }
}