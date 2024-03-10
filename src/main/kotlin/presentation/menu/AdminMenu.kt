package presentation.menu

import data.entity.AccountEntity
import data.entity.AccountType
import domain.controllers.interfaces.AuthenticationController
import presentation.menu.interfaces.DisplayStrategy
import presentation.menu.interfaces.Menu
import presentation.menu.interfaces.RequestOptionStrategy
import presentation.menu.options.AdminMenuOption


class AdminMenu(
    private val authenticationController: AuthenticationController,
    private val menuModificationMenu: Menu,
    private val statisticsMenu: Menu,
    private val userAccount: AccountEntity,
    private val displayStrategy: DisplayStrategy = DefaultDisplayStrategy(AdminMenuOption::class.java),
    private val requestStrategy: RequestOptionStrategy<AdminMenuOption> = ConsoleRequestOptionStrategy(AdminMenuOption::class.java),
) : Menu {
    init {
        if (userAccount.accountType != AccountType.Administrator)
            throw IllegalAccessException("Account ${userAccount.name} has no access right to view this menu.")
    }

    override fun displayMenu() = displayStrategy.display()

    override fun handleInteractions() {
        var isActive = true
        do {
            println("Choose one of the following options.")
            displayMenu()
            when (requestStrategy.requestOption()) {

                AdminMenuOption.AddNewAdminAccount -> println(
                    authenticationController.registerAdminAccount(queryingAccount = userAccount).first
                )

                AdminMenuOption.OpenMenuModificationMenu -> menuModificationMenu.handleInteractions()
                AdminMenuOption.OpenStatisticsMenu -> statisticsMenu.handleInteractions()
                AdminMenuOption.LogOut -> {
                    println("Logging out...")
                    isActive = false
                }

                null -> {}
            }

        } while (isActive)
    }
}
