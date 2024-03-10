package domain.controllers

import data.dao.interfaces.OrderDao
import data.dao.interfaces.RestaurantStatisticsDao
import data.dao.interfaces.ReviewDao
import domain.InputManager
import domain.controllers.interfaces.StatisticsController
import presentation.model.OutputModel
import presentation.model.Status
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class StatisticsControllerImpl(
    private val statisticsDao: RestaurantStatisticsDao,
    private val reviewDao: ReviewDao,
    private val orderDao: OrderDao,
    private val inputManager: InputManager,
) : StatisticsController {
    override fun getRevenue(): OutputModel {
        val statistics = statisticsDao.getStatistics()
            ?: return OutputModel(
                status = Status.Failure,
                message = "No information about the restaurant's revenue found."
            )
        return OutputModel("Restaurant's revenue: ${statistics.revenue}")
    }

    override fun getDishReviews(): OutputModel {
        val reviews = reviewDao.getAllReviews()
        if (reviews.isEmpty())
            return OutputModel("No reviews found.")

        val sb = StringBuilder()
        sb.append("Reviews:\n")
        for ((dishName, reviewList) in reviews.groupBy { it.dishName }) {
            sb.append("\t$dishName:\n")
            sb.append(
                reviewList
                    .sortedBy { it.timeStamp }
                    .joinToString(prefix = "\t\t", separator = "\n\t\t")
                    { "Rating: ${it.rating}, Review: ${it.text}" }
            )
            sb.append('\n')
        }
        return OutputModel(sb.toString())
    }

    override fun getPopularDishes(): OutputModel {
        val orders = orderDao.getAllOrders()
        if (orders.isEmpty())
            return OutputModel("No information about orders found. Cannot show popular dishes.")

        val dishCounts = orders
            .flatMap { it.dishes }
            .groupingBy { it.name }
            .eachCount()
            .toList()
            .sortedByDescending { it.second }

        inputManager.showPrompt("Enter the number of popular dishes to be shown: ")
        val popularDishCount = inputManager.getInt().coerceIn(0, dishCounts.size)
        val popularDishes = dishCounts.take(popularDishCount)
        return OutputModel(
            popularDishes.joinToString(
                prefix = "Most popular dishes: \n\t",
                separator = "\n\t"
            ) { "Dish: \"${it.first}\", Number of ordered dishes: ${it.second}" })
    }

    override fun getAverageRatingOfDishes(): OutputModel {
        val reviews = reviewDao.getAllReviews()
        if (reviews.isEmpty())
            return OutputModel("No information about reviews found. Cannot calculate the average rating.")

        return OutputModel("Average rating of dishes: ${reviews.map { it.rating }.average()}")
    }

    override fun getOrderCountOverPeriod(): OutputModel {
        val orders = orderDao.getAllOrders()
        if (orders.isEmpty())
            return OutputModel("No information about orders found. Cannot count the number of orders.")

        val startDateTime: LocalDateTime
        val endDateTime: LocalDateTime

        try {
            inputManager.showPrompt("Enter the date and time of the start of the period in the following format [yyyy-MM-dd]T[hh:mm:ss]: ")
            startDateTime = LocalDateTime.parse(inputManager.getString())

            inputManager.showPrompt("Enter the date and time of the end of the period in the following format [yyyy-MM-dd]T[hh:mm:ss]: ")
            endDateTime = LocalDateTime.parse(inputManager.getString())
        } catch (ex: DateTimeParseException) {
            return OutputModel("Unable to parse the date.", Status.Failure)
        }

        return OutputModel(
            "The number of orders completed during the specified time interval: ${
                orders.count {
                    belongsToInterval(
                        it.finishTime,
                        startDateTime,
                        endDateTime
                    )
                }
            }"
        )
    }

    private fun belongsToInterval(
        dateTime: LocalDateTime,
        intervalStart: LocalDateTime,
        intervalEnd: LocalDateTime
    ): Boolean {
        return dateTime.isEqual(intervalStart)
                || dateTime.isEqual(intervalEnd)
                || (dateTime.isAfter(intervalStart) && dateTime.isBefore(intervalEnd))
    }
}