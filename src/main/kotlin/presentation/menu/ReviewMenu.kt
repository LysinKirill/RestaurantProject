package presentation.menu

import data.dao.interfaces.OrderDao
import data.entity.AccountEntity
import data.entity.DishEntity
import data.entity.OrderStatus
import domain.controllers.ReviewController
import presentation.menu.options.ReviewMenuOption
import presentation.model.Status


class ReviewMenu(
    private val reviewController: ReviewController,
    private val orderDao: OrderDao,
    private val account: AccountEntity
) : Menu {
    override fun displayMenu() {
        println(
            ReviewMenuOption.entries
                .mapIndexed { index, entry -> "\t${index + 1}. $entry" }
                .joinToString(separator = "\n")
        )
    }

    override fun handleInteractions() {
        var isActive = true
        do {
            println("Choose one of the following options.")
            displayMenu()
            when (getOption()) {
                ReviewMenuOption.ShowReviews -> showReviews()
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

    private fun getOption(): ReviewMenuOption? {
        return readlnOrNull()?.let { parseAction(it) }
    }

    private fun parseAction(userInput: String): ReviewMenuOption? {
        try {
            val optionNumber = userInput.toInt() - 1
            if (optionNumber >= ReviewMenuOption.entries.size || optionNumber < 0) {
                println("Incorrect action chosen...")
                return null
            }
            return ReviewMenuOption.entries[optionNumber]
        } catch (ex: Exception) {
            return null
        }
    }

    private fun deleteReview() {
        if (showReviews() == Status.Failure)
            return
        println(reviewController.deleteReview(account))
    }


    private fun updateReview() {
        if (showReviews() == Status.Failure)
            return
        println(reviewController.editReview(account))
    }

    private fun showReviews(): Status {
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
