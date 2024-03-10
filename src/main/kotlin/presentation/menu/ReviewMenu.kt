package presentation.menu

import data.dao.interfaces.OrderDao
import data.entity.AccountEntity
import data.entity.DishEntity
import data.entity.OrderStatus
import domain.controllers.interfaces.ReviewController
import presentation.menu.interfaces.DisplayStrategy
import presentation.menu.interfaces.Menu
import presentation.menu.interfaces.RequestOptionStrategy
import presentation.menu.options.ReviewMenuOption
import presentation.model.Status


class ReviewMenu(
    private val reviewController: ReviewController,
    private val orderDao: OrderDao,
    private val account: AccountEntity,
    private val displayStrategy: DisplayStrategy = DefaultDisplayStrategy(ReviewMenuOption::class.java),
    private val requestStrategy: RequestOptionStrategy<ReviewMenuOption> = ConsoleRequestOptionStrategy(ReviewMenuOption::class.java),
) : Menu {
    override fun displayMenu() = displayStrategy.display()

    override fun handleInteractions() {
        var isActive = true
        do {
            println("Choose one of the following options.")
            displayMenu()
            when (requestStrategy.requestOption()) {
                ReviewMenuOption.ShowReviews -> tryShowReviews()
                ReviewMenuOption.LeaveReview -> leaveReview()
                ReviewMenuOption.UpdateReview -> updateReview()
                ReviewMenuOption.DeleteReview -> deleteReview()
                ReviewMenuOption.CloseMenu -> {
                    println("Closing review menu...")
                    isActive = false
                }

                null -> {}
            }
        } while (isActive)
    }

    private fun deleteReview() {
        if (tryShowReviews() == Status.Failure)
            return
        println(reviewController.deleteReview(account))
    }


    private fun updateReview() {
        if (tryShowReviews() == Status.Failure)
            return
        println(reviewController.editReview(account))
    }

    private fun tryShowReviews(): Status {
        val response = reviewController.getDishReviews(account)
        println(response)
        return response.status
    }

    private fun leaveReview() {
        val paidDishes = getPaidDishes()
        if (paidDishes.isEmpty()) {
            println("You have no paid orders. Cannot leave a review.")
            return
        }
        showDishes(paidDishes, "Paid dishes:")
        println(reviewController.leaveReview(account))
    }

    private fun getPaidDishes(): List<DishEntity> {
        return orderDao
            .getAllOrders()
            .filter { it.visitorAccountName == account.name && it.status == OrderStatus.PaidFor }
            .flatMap { it.dishes }
            .distinctBy { it.name }
            .sortedBy { it.name }
    }

    private fun showDishes(dishes: List<DishEntity>, prompt: String) {
        print(prompt + "\n\t")
        println(dishes.joinToString(separator = "\n\t") { it.name })
    }
}
