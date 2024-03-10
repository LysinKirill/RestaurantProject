package domain.services

import data.entity.OrderEntity
import data.entity.OrderStatus
import java.util.*

class ThreadSafeQueueOrderScheduler : OrderScheduler {
    private val lock = Any()
    private val orderQueue: Queue<OrderEntity> = LinkedList()
    override fun scheduleOrder(orderEntity: OrderEntity) {
        synchronized(lock) {
            if (orderEntity.status == OrderStatus.Created || orderEntity.status == OrderStatus.Cooking) {
                orderQueue.add(orderEntity)

                //DELETE
                //println("Order (id = ${orderEntity.id}) scheduled.")
            }
        }
    }

    override fun getOrder(): OrderEntity? {
        synchronized(lock) {
            if (orderQueue.isEmpty())
                return null

            //DELETE
            //println("Order (id = ${orderQueue.peek().id}) removed from queue.")

            return orderQueue.remove()
        }
    }
}