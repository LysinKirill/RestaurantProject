package domain

import data.entity.OrderEntity

interface CookingStrategy {
    fun cookOrder(order: OrderEntity)
}