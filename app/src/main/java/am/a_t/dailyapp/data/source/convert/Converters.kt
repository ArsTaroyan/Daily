package am.a_t.dailyapp.data.source.convert

import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.extension.convertGsonToString
import am.a_t.dailyapp.extension.convertStringToGson
import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromCalendar(calendar: Calendar?): String = calendar.convertGsonToString()

    @TypeConverter
    fun toCalendar(str: String): Calendar? = str.convertStringToGson()

    @TypeConverter
    fun fromList(list: ArrayList<Todo>?): String = list.convertGsonToString()

    @TypeConverter
    fun toList(str: String): ArrayList<Todo>? = str.convertStringToGson()
}