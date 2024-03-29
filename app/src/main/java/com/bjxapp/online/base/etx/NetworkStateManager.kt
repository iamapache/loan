package com.bjxapp.online.base.etx

import com.bjxapp.online.base.livedata.EventLiveData

/**
 * 项目：  Lemon Cash
 * 类名：  NetworkStateManager.java
 * 时间：  2024/3/28 0028 19:05
 * 描述：
 */
class NetworkStateManager private constructor() {

    val mNetworkStateCallback = EventLiveData<NetState>()

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }

}