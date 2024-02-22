package domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class OrderEntity(val id: Int, val dishes: List<DishEntity>)