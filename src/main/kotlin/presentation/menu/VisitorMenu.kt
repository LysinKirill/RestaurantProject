package presentation.menu

import domain.OrderProcessingSystem
import domain.controllers.RestaurantMenuController
import data.entity.AccountEntity
import presentation.model.Status

class VisitorMenu(
    private val menuController: RestaurantMenuController,
    private val orderSystem: OrderProcessingSystem,
    private val userAccount: AccountEntity
) : Menu {
    override fun displayMenu() {
        println("Options:")
        println(
            VisitorMenuOption.entries
                .mapIndexed { index, entry -> "\t${index + 1}. $entry" }
                .joinToString(separator = "\n")
        )
    }

    override fun handleInteractions() {
        var isActive = true
        do {
            println("Choose one of the following options.")
            displayMenu()
            when (getOption()) {
                VisitorMenuOption.CreateOrder -> {
                    val response = menuController.getAvailableDishes()
                    println(response.message)
                    if(response.status == Status.Failure)
                    {
                        println("Unable to create the order.")
                        continue
                    }
                    orderSystem.createOrder(userAccount)
                }

                VisitorMenuOption.ShowOrders -> orderSystem.showUserOrders(userAccount)
                VisitorMenuOption.AddDishToOrder -> orderSystem.addDishToOrder(userAccount)
                VisitorMenuOption.CancelOrder -> orderSystem.cancelOrder(userAccount)
                VisitorMenuOption.PayForOrder -> orderSystem.payForOrder(userAccount)
                VisitorMenuOption.LogOut -> {
                    println("Logging out...")
                    isActive = false
                }

                null -> {}

            }

        } while (isActive)
    }

    private fun getOption(): VisitorMenuOption? {
        return readlnOrNull()?.let { parseAction(it) }
    }

    private fun parseAction(userInput: String): VisitorMenuOption? {
        try {
            val optionNumber = userInput.toInt() - 1
            if (optionNumber >= VisitorMenuOption.entries.size || optionNumber < 0) {
                println("Incorrect action chosen...")
                return null
            }
            return VisitorMenuOption.entries[optionNumber]
        } catch (ex: Exception) {
            return null
        }
    }
}
