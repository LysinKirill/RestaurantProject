package domain

import data.RestaurantStatisticsDao
import data.entity.AccountEntity
import di.DI

class PaymentServiceImpl(
    private val statisticsDao: RestaurantStatisticsDao
) : PaymentService {
    override fun receivePayment(account: AccountEntity, paymentAmount: Double): Boolean {
        println("Processing the transaction...")
        val statistics = statisticsDao.getStatistics()
        if(statistics == null) {
            println("Cannot process the transaction.")
            return false
        }
        DI.inputManager.showPrompt("Confirm transaction: [Yes]/[No]")
        val confirmation = DI.inputManager.getString()
        if(confirmation.lowercase() != "yes")
        {
            println("The transaction has been cancelled.")
            return false
        }
        statisticsDao.saveStatistics(statistics.copy(revenue = statistics.revenue + paymentAmount))
        println("The transaction is completed.")
        return true
    }

    override fun requestPayment(requestPrompt: String) {
        println(requestPrompt)
    }
}