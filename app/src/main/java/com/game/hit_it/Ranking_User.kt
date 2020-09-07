package com.game.hit_it


import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ranking_User(val profileImageUrl: String = "", val username: String = "", var score: String= "0") : Parcelable {

}