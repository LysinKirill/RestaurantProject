package domain

import data.entity.OrderEntity
import java.time.LocalDateTime
import java.time.ZoneOffset

class SleepCookingStrategy : CookingStrategy {
    override fun cookOrder(order: OrderEntity) {
        val startTimeInstant = LocalDateTime.now().toInstant(ZoneOffset.UTC)
        val finishTimeInstant = order.finishTime.toInstant(ZoneOffset.UTC)
        val cookingTime: java.time.Duration = java.time.Duration.between(startTimeInstant, finishTimeInstant) ?: return

        Thread.sleep(cookingTime.toMillis())
    }
}