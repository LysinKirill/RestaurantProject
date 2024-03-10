package domain.controllers.interfaces

import presentation.model.OutputModel

interface StatisticsController {
    fun getRevenue(): OutputModel
    fun getDishReviews(): OutputModel
    fun getPopularDishes(): OutputModel
    fun getAverageRatingOfDishes(): OutputModel
    fun getOrderCountOverPeriod(): OutputModel
}