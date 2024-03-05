package presentation.menu

import domain.controllers.StatisticsController
import presentation.menu.options.StatisticsMenuOption

class StatisticsMenu(
    private val statisticsController: StatisticsController,
    private val displayStrategy: DisplayStrategy = DefaultDisplayStrategy(StatisticsMenuOption::class.java)) : Menu {
    override fun displayMenu() = displayStrategy.display()

    override fun handleInteractions() {
        var isActive = true
        do {
            println("Choose one of the following options.")
            displayMenu()
            when (getOption()) {
                null -> {}
                StatisticsMenuOption.Revenue -> println(statisticsController.getRevenue())
                StatisticsMenuOption.PopularDishes -> println(statisticsController.getPopularDishes())
                StatisticsMenuOption.AverageRatingOfDishes -> println(statisticsController.getAverageRatingOfDishes())
                StatisticsMenuOption.NumberOfOrdersOverPeriod -> println(statisticsController.getOrderCountOverPeriod())
                StatisticsMenuOption.CloseMenu -> {
                    println("Closing statistics menu...")
                    isActive = false
                }
            }
        } while (isActive)
    }


    private fun getOption(): StatisticsMenuOption? {
        return readlnOrNull()?.let { parseAction(it) }
    }

    private fun parseAction(userInput: String): StatisticsMenuOption? {
        try {
            val optionNumber = userInput.toInt() - 1
            if (optionNumber >= StatisticsMenuOption.entries.size || optionNumber < 0) {
                println("Incorrect action chosen...")
                return null
            }
            return StatisticsMenuOption.entries[optionNumber]
        } catch (ex: Exception) {
            return null
        }
    }
}