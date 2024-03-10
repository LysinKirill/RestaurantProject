package presentation.menu

import domain.controllers.interfaces.StatisticsController
import presentation.menu.interfaces.DisplayStrategy
import presentation.menu.interfaces.Menu
import presentation.menu.interfaces.RequestOptionStrategy
import presentation.menu.options.StatisticsMenuOption

class StatisticsMenu(
    private val statisticsController: StatisticsController,
    private val displayStrategy: DisplayStrategy = DefaultDisplayStrategy(StatisticsMenuOption::class.java),
    private val requestStrategy: RequestOptionStrategy<StatisticsMenuOption> = ConsoleRequestOptionStrategy(
        StatisticsMenuOption::class.java
    ),
) : Menu {
    override fun displayMenu() = displayStrategy.display()

    override fun handleInteractions() {
        var isActive = true
        do {
            println("Choose one of the following options.")
            displayMenu()
            when (requestStrategy.requestOption()) {
                null -> {}
                StatisticsMenuOption.Revenue -> println(statisticsController.getRevenue())
                StatisticsMenuOption.PopularDishes -> println(statisticsController.getPopularDishes())
                StatisticsMenuOption.AverageRatingOfDishes -> println(statisticsController.getAverageRatingOfDishes())
                StatisticsMenuOption.NumberOfOrdersOverPeriod -> println(statisticsController.getOrderCountOverPeriod())
                StatisticsMenuOption.ShowDishReviews -> println(statisticsController.getDishReviews())
                StatisticsMenuOption.CloseMenu -> {
                    println("Closing statistics menu...")
                    isActive = false
                }
            }
        } while (isActive)
    }
}