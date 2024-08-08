package domain.models

data class Stop(
    val id: String,
    val name: String,
    val region: String = "Dresden",
    var isFavorite: Boolean = false
)