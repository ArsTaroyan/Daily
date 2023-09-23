package am.a_t.dailyapp.extension

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson

fun <T> T.convertGsonToString(): String = Gson().toJson(this)

inline fun <reified T> String.convertStringToGson(): T = Gson().fromJson(this, T::class.java)

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}