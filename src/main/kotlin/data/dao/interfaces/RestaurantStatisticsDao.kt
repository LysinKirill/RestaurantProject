package data.dao.interfaces

import data.entity.RestaurantStatisticsEntity

interface RestaurantStatisticsDao {
    fun getStatistics() : RestaurantStatisticsEntity?
    fun saveStatistics(statistics: RestaurantStatisticsEntity)
}