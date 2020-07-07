package com.game.hit_it


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val username: String, val profileImageUrl: String, var score : Int = 0): Parcelable {

}