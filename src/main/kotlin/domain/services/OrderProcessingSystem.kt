package domain.services

import data.entity.AccountEntity
import data.entity.OrderEntity

interface OrderProcessingSystem {
    fun getUserOrders(user: AccountEntity) : List<OrderEntity>
    fun showUserOrders(user: AccountEntity)
    fun createOrder(user: AccountEntity)
    fun addDishToOrder(user: AccountEntity)
    fun cancelOrder(user: AccountEntity)
    fun payForOrder(user: AccountEntity)
    fun clearOrders()
}