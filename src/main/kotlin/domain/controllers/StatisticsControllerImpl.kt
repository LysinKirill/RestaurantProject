package domain.controllers

import data.RestaurantStatisticsDao
import presentation.model.OutputModel
import presentation.model.Status
import java.time.LocalDateTime

class StatisticsControllerImpl(
    private val statisticsDao: RestaurantStatisticsDao
) : StatisticsController {
    override fun getRevenue(): OutputModel {
        val statistics = statisticsDao.getStatistics()
            ?: return OutputModel(
                status = Status.Failure,
                message = "No information about the restaurant's revenue found."
            )
        return OutputModel("Restaurant's revenue: ${statistics.revenue}")
    }

    override fun getDishReviews(dishName: String): OutputModel {
        TODO("Not yet implemented")
    }

    override fun getPopularDishes(numberOfDishes: Int): OutputModel {
        TODO("Not yet implemented")
    }

    override fun getAverageRatingOfDishes(): OutputModel {
        TODO("Not yet implemented")
    }

    override fun getOrderCountOverPeriod(startOfPeriod: LocalDateTime, endOfPeriod: LocalDateTime): OutputModel {
        TODO("Not yet implemented")
    }
}