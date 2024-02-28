package domain

import data.entity.AccountEntity

interface OrderProcessingSystem {
    fun showUserOrders(user: AccountEntity)
    fun createOrder(user: AccountEntity)
    fun addDishToOrder(user: AccountEntity)
    fun cancelOrder(user: AccountEntity)
    fun payForOrder(user: AccountEntity)
    fun clearOrders()
}