package data.entity

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class OrderEntity(
    val id: Int,
    val visitorAccountName: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val finishTime: LocalDateTime,
    val dishes: List<DishEntity>,
    val status: OrderStatus,
)