package presentation.menu

import data.entity.AccountEntity
import domain.services.OrderProcessingSystem
import domain.controllers.interfaces.RestaurantMenuController
import presentation.menu.interfaces.DisplayStrategy
import presentation.menu.interfaces.Menu
import presentation.menu.interfaces.RequestOptionStrategy
import presentation.menu.options.OrderMenuOption
import presentation.model.Status


class OrderMenu(
    private val menuController: RestaurantMenuController,
    private val orderSystem: OrderProcessingSystem,
    private val userAccount: AccountEntity,
    private val displayStrategy: DisplayStrategy = DefaultDisplayStrategy(OrderMenuOption::class.java),
    private val requestStrategy: RequestOptionStrategy<OrderMenuOption> = ConsoleRequestOptionStrategy(OrderMenuOption::class.java),
) : Menu {
    override fun displayMenu() = displayStrategy.display()

    override fun handleInteractions() {
        var isActive = true
        do {
            println("Choose one of the following options.")
            displayMenu()
            when (requestStrategy.requestOption()) {
                OrderMenuOption.CreateOrder -> {
                    val response = menuController.getAvailableDishes()
                    println(response.message)
                    if (response.status == Status.Failure) {
                        println("Unable to create the order.")
                        continue
                    }
                    println()
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
}