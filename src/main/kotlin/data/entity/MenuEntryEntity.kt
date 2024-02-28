package data.entity

import kotlinx.serialization.Serializable

@Serializable
data class MenuEntryEntity (
    val dish: DishEntity,
    val remainingNumber: Int
)