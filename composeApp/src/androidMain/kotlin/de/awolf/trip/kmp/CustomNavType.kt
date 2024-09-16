package de.awolf.trip.kmp

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import de.awolf.trip.kmp.core.domain.models.PickableDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import de.awolf.trip.kmp.core.domain.models.Stop

object CustomNavType {

    val StopType = object : NavType<Stop>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): Stop? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Stop {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Stop): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Stop) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }

    val PickableDateTimeType = object : NavType<PickableDateTime>(
        isNullableAllowed = true
    ) {
        override fun get(bundle: Bundle, key: String): PickableDateTime? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): PickableDateTime {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: PickableDateTime): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: PickableDateTime) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}