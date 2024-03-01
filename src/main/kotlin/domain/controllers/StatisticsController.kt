package domain.controllers

import presentation.model.OutputModel

interface StatisticsController {
    fun getRevenue(): OutputModel
    fun getDishReviews(): OutputModel
    fun getPopularDishes(): OutputModel
    fun getAverageRatingOfDishes(): OutputModel
    fun getOrderCountOverPeriod(): OutputModel // Maybe should use Period or DatePeriod
}