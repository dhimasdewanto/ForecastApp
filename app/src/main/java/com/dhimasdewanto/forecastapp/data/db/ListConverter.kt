package com.dhimasdewanto.forecastapp.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson

class ListConverter {
    @TypeConverter
    fun listToJson(listString: List<String>?): String = Gson().toJson(listString)

    @TypeConverter
    fun jsonToList(value: String?) = Gson().fromJson(value, Array<String>::class.java).toList()

}