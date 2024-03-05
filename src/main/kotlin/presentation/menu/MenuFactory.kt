package presentation.menu

import data.entity.AccountEntity
import data.entity.AccountType
import di.DI

class MenuFactory {
    fun getAuthenticationMenu(): ResponsiveMenu<AccountEntity> {
        return AuthenticationMenu(DI.authenticationController)
    }


    fun getMenuForUser(account: AccountEntity): Menu {
        return when (account.accountType) {
            AccountType.Administrator -> AdminMenu(
                menuController = DI.menuController,
                authenticationController = DI.authenticationController,
                statisticsMenu = getStatisticsMenu(),
                userAccount = account
            )

            AccountType.Visitor -> VisitorMenu(
                reviewMenu = getReviewMenu(account),
                orderMenu = getOrderMenu(account),
            )
        }
    }

    private fun getReviewMenu(account: AccountEntity) = ReviewMenu(
        reviewController = DI.reviewController,
        orderDao = DI.orderDao,
        account = account,
    )

    private fun getOrderMenu(account: AccountEntity) = OrderMenu(
        menuController = DI.menuController,
        orderSystem = DI.orderSystem,
        userAccount = account,
    )

    private fun getStatisticsMenu() = StatisticsMenu(DI.statisticsController)
}