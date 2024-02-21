package presentation.menu

import di.DI
import domain.entity.AccountEntity
import domain.entity.AccountType

class MenuFactory {
    fun getAuthenticationMenu(): ResponsiveMenu<AccountEntity> {
        return AuthenticationMenu(DI.authenticationController)
    }


    fun getMenuForRole(accountType: AccountType): Menu {
        return when (accountType) {
            AccountType.Administrator -> AdminMenu()
            AccountType.Visitor -> VisitorMenu()
        }
    }
}