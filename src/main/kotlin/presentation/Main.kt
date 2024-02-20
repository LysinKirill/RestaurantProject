package presentation

import di.DI
import domain.entity.Account
import domain.entity.AccountType


fun main() {
    try {
        val menuFactory = MenuFactory()
        val authenticationMenu = menuFactory.getAuthenticationMenu(DI.authenticator)
        authenticationMenu.handleInteractions()
        val account = authenticationMenu.getResponse()
        if(account == null)
        {
            println("Authentication failed. Exiting...")
            return
        }

        val menu = menuFactory.getMenuForRole(account.accountType)
        menu.handleInteractions()


    } catch (ex: Exception) {
        println("Unexpected exception has occurred: ${ex.message}")
    }
}
