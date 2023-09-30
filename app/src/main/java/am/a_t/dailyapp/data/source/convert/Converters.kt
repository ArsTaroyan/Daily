package am.a_t.dailyapp.data.source.convert

import am.a_t.dailyapp.extension.convertGsonToString
import am.a_t.dailyapp.extension.convertStringToGson
import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromCalendar(calendar: Calendar): String = calendar.convertGsonToString()

    @TypeConverter
    fun toCalendar(str: String): Calendar = str.convertStringToGson()
}