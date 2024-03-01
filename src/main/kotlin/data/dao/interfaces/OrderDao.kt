package data.dao.interfaces

import data.entity.DishEntity
import data.entity.OrderEntity
import java.time.LocalDateTime

interface OrderDao {
    fun addOrder(visitorAccountName: String, finishTime: LocalDateTime, dishes: List<DishEntity>) : OrderEntity?
    fun getOrder(orderId: Int) : OrderEntity?
    fun removeOrder(orderId: Int)
    fun getAllOrders() : List<OrderEntity>
    fun updateOrder(updatedOrder: OrderEntity)
}