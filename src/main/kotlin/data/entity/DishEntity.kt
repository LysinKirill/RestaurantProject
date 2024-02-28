package data.entity

import kotlinx.serialization.Serializable

@Serializable
data class DishEntity(
    val name: String,
    val price: Double,
    val cookingTimeInSeconds: Int
) {
    override fun toString(): String =
        "Dish(Name: $name, Price: $price, Cooking time: $cookingTimeInSeconds seconds)"
}
