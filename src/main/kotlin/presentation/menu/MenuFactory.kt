package presentation.menu

import di.DI
import domain.entity.AccountEntity
import domain.entity.AccountType

class MenuFactory {
    fun getAuthenticationMenu(): ResponsiveMenu<AccountEntity> {
        return AuthenticationMenu(DI.authenticationController)
    }


    fun getMenuForUser(account: AccountEntity): Menu {
        return when (account.accountType) {
            AccountType.Administrator -> AdminMenu(DI.menuController, DI.authenticationController, account)
            AccountType.Visitor -> VisitorMenu(account)
        }
    }
}