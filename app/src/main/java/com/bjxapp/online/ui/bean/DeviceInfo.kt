package com.bjxapp.online.ui.bean

/**
 *   Lemon Cash
 *  DeviceInfo.java
 *
 */
data class DeviceInfo(
    val androidIdOrUdid: String,
    val appVersion: String,
    val bag: String,
    val brand: String,
    val channel: String,
    val deviceModel: String,
    val gaidOrIdfa: String,
    val mac: String,
    val operationSys: String,
    val osVersion: String
)