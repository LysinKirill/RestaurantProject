package presentation.menu

import domain.controllers.interfaces.RestaurantMenuController
import presentation.menu.interfaces.DisplayStrategy
import presentation.menu.interfaces.Menu
import presentation.menu.interfaces.RequestOptionStrategy
import presentation.menu.options.MenuModificationMenuOption

class MenuModificationMenu(
    private val menuController: RestaurantMenuController,
    private val displayStrategy: DisplayStrategy =
        DefaultDisplayStrategy(MenuModificationMenuOption::class.java),
    private val requestStrategy: RequestOptionStrategy<MenuModificationMenuOption> =
        ConsoleRequestOptionStrategy(MenuModificationMenuOption::class.java),
) : Menu {
    override fun displayMenu() = displayStrategy.display()

    override fun handleInteractions() {
        var isActive = true
        do {
            println("Choose one of the following options.")
            displayMenu()
            when (requestStrategy.requestOption()) {
                MenuModificationMenuOption.AddDishToMenu -> println(menuController.addMenuEntry())
                MenuModificationMenuOption.RemoveDishFromMenu -> println(menuController.removeMenuEntry())
                MenuModificationMenuOption.SetDishCount -> println(menuController.changeDishCount())
                MenuModificationMenuOption.SetDishPrice -> println(menuController.changeDishPrice())
                MenuModificationMenuOption.SetDishCookingTime -> println(menuController.changeDishCookingTime())
                MenuModificationMenuOption.GetAllMenuEntries -> println(menuController.getAllMenuEntries())
                MenuModificationMenuOption.CloseMenu -> {
                    println("Exiting menu modification menu...")
                    isActive = false
                }

                null -> {}
            }
        } while (isActive)
    }
}