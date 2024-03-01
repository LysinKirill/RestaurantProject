package data.entity

data class ReviewEntity(
    val id: Long,
    val dishName: String,
    val text: String,
    val rating: Byte
)