package presentation

import di.DI
import domain.Authenticator
import domain.entity.Account
import domain.entity.AccountType

class MenuFactory {
    fun getAuthenticationMenu(authenticator: Authenticator<String>): ResponsiveMenu<Account> {
        return AuthenticationMenu(DI.authenticationController)
    }


    fun getMenuForRole(accountType: AccountType): Menu {
        return when (accountType) {
            AccountType.Administrator -> AdminMenu()
            AccountType.Visitor -> VisitorMenu()
        }
    }
}