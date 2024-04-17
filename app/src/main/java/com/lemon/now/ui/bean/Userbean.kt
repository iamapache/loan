package com.lemon.now.ui.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *   Lemon Cash
 *  Userbean.java
 *
 */
@Parcelize
data class Userbean (
    var userNames: String="",
    var userNumber: String=""
    ,
    var male: String=""
    ,
    var date: String=""
    ,
    var address: String=""
    ,
    var panNumber: String=""
    ,
    var frontimg: String=""
    ,
    var backimg: String="",
    var cardImg: String=""
): Parcelable