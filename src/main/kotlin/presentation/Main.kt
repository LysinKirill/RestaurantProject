package presentation

import presentation.menu.MenuFactory


fun main() {
    try {
        val menuFactory = MenuFactory()
        val authenticationMenu = menuFactory.getAuthenticationMenu()
        authenticationMenu.handleInteractions()
        val account = authenticationMenu.getResponse()

        if(account == null)
        {
            println("Exiting...")
            return
        }

        val menu = menuFactory.getMenuForUser(account)
        menu.handleInteractions()
    } catch (ex: Exception) {
        println("Unexpected exception has occurred: ${ex.message}")
    }
}
