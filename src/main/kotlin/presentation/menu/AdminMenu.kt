package presentation.menu

import data.entity.AccountEntity
import data.entity.AccountType
import domain.controllers.AuthenticationController
import domain.controllers.RestaurantMenuController
import domain.controllers.StatisticsController


class AdminMenu(
    private val menuController: RestaurantMenuController,
    private val authenticationController: AuthenticationController,
    private val statisticsController: StatisticsController,
    private val userAccount: AccountEntity
) : Menu {
    init {
        if (userAccount.accountType != AccountType.Administrator)
        // Change the type of the exception to something like IllegalAccess exception
        // or get rid of it altogether
            throw Exception("Account ${userAccount.name} has no access right to view this menu.")
    }

    private var isActive = true
    override fun displayMenu() {
        println(
            AdminMenuOption.entries
                .mapIndexed { index, entry -> "\t${index + 1}. $entry" }
                .joinToString(separator = "\n")
        )
    }

    override fun handleInteractions() {
        isActive = true
        do {
            println("Choose one of the following options.")
            displayMenu()
            when (getOption()) {
                AdminMenuOption.AddDishToMenu -> println(menuController.addMenuEntry())
                AdminMenuOption.RemoveDishFromMenu -> println(menuController.removeMenuEntry())
                AdminMenuOption.SetDishCount -> println(menuController.changeDishCount())
                AdminMenuOption.SetDishPrice -> println(menuController.changeDishPrice())
                AdminMenuOption.SetDishCookingTime -> println(menuController.changeDishCookingTime())
                AdminMenuOption.GetAllMenuEntries -> println(menuController.getAllMenuEntries())
                AdminMenuOption.AddNewAdminAccount -> println(
                    authenticationController.registerAdminAccount(queryingAccount = userAccount).first
                )

                // Add a separate menu for statistics
                AdminMenuOption.GetRestaurantRevenue -> println(statisticsController.getRevenue())
                AdminMenuOption.LogOut -> {
                    println("Logging out...")
                    isActive = false
                }

                null -> {}
            }

        } while (isActive)
    }


    private fun getOption(): AdminMenuOption? {
        return readlnOrNull()?.let { parseAction(it) }
    }

    private fun parseAction(userInput: String): AdminMenuOption? {
        try {
            val optionNumber = userInput.toInt() - 1
            if (optionNumber >= AdminMenuOption.entries.size || optionNumber < 0) {
                println("Incorrect action chosen...")
                return null
            }
            return AdminMenuOption.entries[optionNumber]
        } catch (ex: Exception) {
            return null
        }
    }
}
