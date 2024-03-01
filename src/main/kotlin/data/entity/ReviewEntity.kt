package data.entity

import java.time.LocalDateTime

data class ReviewEntity(
    val id: Long,
    val accountName: String,
    val dishName: String,
    val text: String,
    val rating: Byte,
    val timeStamp: LocalDateTime,
)