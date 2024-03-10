package presentation

import di.DI
import presentation.menu.MenuFactory


fun main() {
    try {
        val menuFactory = MenuFactory()

        while (true) {
            val authenticationMenu = menuFactory.getAuthenticationMenu()
            authenticationMenu.handleInteractions()
            val account = authenticationMenu.getResponse()

            if (account == null) {
                println("Exiting...")
                DI.orderSystem.clearOrders()
                return
            }

            val menu = menuFactory.getMenuForUser(account)
            menu.handleInteractions()
        }
    } catch (ex: Exception) {
        println("Unexpected exception has occurred: ${ex.message}")
    }
}
