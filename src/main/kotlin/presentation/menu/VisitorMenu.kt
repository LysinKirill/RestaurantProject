package presentation.menu

import presentation.menu.interfaces.DisplayStrategy
import presentation.menu.interfaces.Menu
import presentation.menu.interfaces.RequestOptionStrategy
import presentation.menu.options.VisitorMenuOption

class VisitorMenu(
    private val reviewMenu: ReviewMenu,
    private val orderMenu: OrderMenu,
    private val displayStrategy: DisplayStrategy = DefaultDisplayStrategy(VisitorMenuOption::class.java),
    private val requestStrategy: RequestOptionStrategy<VisitorMenuOption> = ConsoleRequestOptionStrategy(VisitorMenuOption::class.java),
) : Menu {
    override fun displayMenu() = displayStrategy.display()

    override fun handleInteractions() {
        var isActive = true
        do {
            println("Choose one of the following options.")
            displayMenu()
            when (requestStrategy.requestOption()) {
                VisitorMenuOption.OpenOrderMenu -> orderMenu.handleInteractions()
                VisitorMenuOption.OpenReviewMenu -> reviewMenu.handleInteractions()
                VisitorMenuOption.LogOut -> {
                    println("Logging out...")
                    isActive = false
                }

                null -> {}
            }

        } while (isActive)
    }
}
