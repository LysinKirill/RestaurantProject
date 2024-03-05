package presentation.menu

import data.entity.AccountEntity
import domain.OrderProcessingSystem
import domain.controllers.RestaurantMenuController
import presentation.menu.options.OrderMenuOption
import presentation.model.Status


class OrderMenu(
    private val menuController: RestaurantMenuController,
    private val orderSystem: OrderProcessingSystem,
    private val userAccount: AccountEntity,
    private val displayStrategy: DisplayStrategy = DefaultDisplayStrategy(OrderMenuOption::class.java)
) : Menu {
    override fun displayMenu() = displayStrategy.display()

    override fun handleInteractions() {
        var isActive = true
        do {
            println("Choose one of the following options.")
            displayMenu()
            when (getOption()) {
                OrderMenuOption.CreateOrder -> {
                    val response = menuController.getAvailableDishes()
                    println(response.message)
                    if (response.status == Status.Failure) {
                        println("Unable to create the order.")
                        continue
                    }
                    orderSystem.createOrder(userAccount)
                }

                OrderMenuOption.ShowOrders -> orderSystem.showUserOrders(userAccount)
                OrderMenuOption.AddDishToOrder -> orderSystem.addDishToOrder(userAccount)
                OrderMenuOption.CancelOrder -> orderSystem.cancelOrder(userAccount)
                OrderMenuOption.PayForOrder -> orderSystem.payForOrder(userAccount)
                OrderMenuOption.CloseMenu -> {
                    println("Exiting order menu...")
                    isActive = false
                }

                null -> {}
            }

        } while (isActive)
    }

    private fun getOption(): OrderMenuOption? {
        return readlnOrNull()?.let { parseAction(it) }
    }

    private fun parseAction(userInput: String): OrderMenuOption? {
        try {
            val optionNumber = userInput.toInt() - 1
            if (optionNumber >= OrderMenuOption.entries.size || optionNumber < 0) {
                println("Incorrect action chosen...")
                return null
            }
            return OrderMenuOption.entries[optionNumber]
        } catch (ex: Exception) {
            return null
        }
    }
}