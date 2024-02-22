package presentation.menu

import domain.AuthenticationController
import domain.RestaurantMenuController
import domain.entity.AccountEntity
import domain.entity.AccountType


class AdminMenu(
    private val menuController: RestaurantMenuController,
    private val authenticationController: AuthenticationController,
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
            val optionNumber = userInput.toInt()
            return when (optionNumber) {
                1 -> AdminMenuOption.AddDishToMenu
                2 -> AdminMenuOption.RemoveDishFromMenu
                3 -> AdminMenuOption.SetDishCount
                4 -> AdminMenuOption.SetDishPrice
                5 -> AdminMenuOption.SetDishCookingTime
                6 -> AdminMenuOption.GetAllMenuEntries
                7 -> AdminMenuOption.AddNewAdminAccount
                8 -> AdminMenuOption.LogOut
                else -> run {
                    println("Incorrect action chosen...")
                    null
                }
            }
        } catch (ex: Exception) {
            return null
        }
    }
}
