package domain.controllers

import presentation.model.OutputModel
import java.time.LocalDateTime

interface StatisticsController {
    fun getRevenue() : OutputModel
    fun getDishReviews(dishName: String) : OutputModel
    fun getPopularDishes(numberOfDishes: Int) : OutputModel
    fun getAverageRatingOfDishes() : OutputModel
    fun getOrderCountOverPeriod(
        startOfPeriod: LocalDateTime,
        endOfPeriod: LocalDateTime
    ) : OutputModel // Maybe should use Period or DatePeriod
}