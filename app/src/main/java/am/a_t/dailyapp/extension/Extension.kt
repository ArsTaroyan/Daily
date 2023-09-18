package am.a_t.dailyapp.extension

import com.google.gson.Gson

fun <T> T.convertGenericToString(): String = Gson().toJson(this)

inline fun <reified T> String.convertStringToGeneric(): T = Gson().fromJson(this, T::class.java)