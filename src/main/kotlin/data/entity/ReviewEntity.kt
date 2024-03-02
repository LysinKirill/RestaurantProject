package data.entity

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ReviewEntity(
    val id: Long,
    val accountName: String,
    val dishName: String,
    val text: String,
    val rating: Byte,
    @Serializable(with = LocalDateTimeSerializer::class)
    val timeStamp: LocalDateTime,
)