package de.awolf.trip.kmp.core.domain.models

data class ResponseStatus(
    val code: String,
    val message: String = "",
) {
    fun isOk(): Boolean {
        return code == "Ok"
    }
}