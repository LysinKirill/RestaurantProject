package presentation.menu

import di.DI
import data.entity.AccountEntity
import data.entity.AccountType

class MenuFactory {
    fun getAuthenticationMenu(): ResponsiveMenu<AccountEntity> {
        return AuthenticationMenu(DI.authenticationController)
    }


    fun getMenuForUser(account: AccountEntity): Menu {
        return when (account.accountType) {
            AccountType.Administrator -> AdminMenu(
                menuController = DI.menuController,
                authenticationController = DI.authenticationController,
                //statisticsController = DI.statisticsController,
                statisticsMenu = StatisticsMenu(DI.statisticsController),
                userAccount = account
            )
            AccountType.Visitor -> VisitorMenu(
                menuController = DI.menuController,
                orderSystem = DI.orderSystem,
                userAccount = account
            )
        }
    }
}