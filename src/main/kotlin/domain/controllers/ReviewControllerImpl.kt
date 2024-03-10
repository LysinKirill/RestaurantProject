package domain.controllers

import data.dao.interfaces.OrderDao
import data.dao.interfaces.ReviewDao
import data.entity.AccountEntity
import data.entity.OrderStatus
import domain.InputManager
import domain.controllers.interfaces.ReviewController
import presentation.model.OutputModel
import presentation.model.Status
import java.time.LocalDateTime

class ReviewControllerImpl(
    private val reviewDao: ReviewDao,
    private val orderDao: OrderDao,
    private val inputManager: InputManager
) : ReviewController {
    override fun getDishReviews(account: AccountEntity): OutputModel {
        val reviews = reviewDao.getAllReviews()
        if (reviews.isEmpty())
            return OutputModel("No reviews found on account ${account.name}.", Status.Failure)
        return OutputModel(
            reviews
                .filter { it.accountName == account.name }
                .joinToString(separator = "\n\t", prefix = "Reviews for account \"${account.name}\": \n\t") {
                    "Review ID: ${it.id}, Dish: \"${it.dishName}\", Rating: ${it.rating}, Review: ${it.text}"
                }
        )
    }

    override fun leaveReview(account: AccountEntity): OutputModel {
        inputManager.showPrompt("Enter the name of the dish you want to leave a review on: ")
        val dishName = inputManager.getString()
        val paidDishes = orderDao
            .getAllOrders()
            .filter { it.visitorAccountName == account.name && it.status == OrderStatus.PaidFor }
            .flatMap { it.dishes }

        if (paidDishes.none { it.name == dishName })
            return OutputModel(
                status = Status.Failure,
                message = "Dish \"$dishName\" was not found in any order you paid for. Cannot leave a review on it."
            )

        val reviewInfo = getReviewDetails()
        if (reviewInfo.first == Status.Failure)
            return OutputModel(reviewInfo.third, Status.Failure)

        reviewDao.addReview(
            accountName = account.name,
            dishName = dishName,
            text = reviewInfo.third,
            rating = reviewInfo.second,
            timeStamp = LocalDateTime.now()
        )
        return OutputModel("Your review has been recorded. Thank you for the feedback!")
    }


    override fun editReview(account: AccountEntity): OutputModel {
        inputManager.showPrompt("Enter the ID of the review to be changed: ")
        val reviewId = inputManager.getInt()

        val review = reviewDao.getReview(reviewId.toLong())
        if (review == null || review.accountName != account.name) return OutputModel(
            status = Status.Failure,
            message = "No record of review with ID = $reviewId for account \"${account.name}\" found."
        )

        val reviewInfo = getReviewDetails()
        if (reviewInfo.first == Status.Failure)
            return OutputModel(reviewInfo.third, Status.Failure)

        reviewDao.updateReview(review.copy(text = reviewInfo.third, rating = reviewInfo.second))
        return OutputModel("Your review has been successfully updated.")
    }

    override fun deleteReview(account: AccountEntity): OutputModel {
        inputManager.showPrompt("Enter the ID of the review to be deleted: ")
        val reviewId = inputManager.getInt()

        val review = reviewDao.getReview(reviewId.toLong())
        if (review == null || review.accountName != account.name) return OutputModel(
            status = Status.Failure,
            message = "No record of review with ID = $reviewId for account \"${account.name}\" found."
        )

        reviewDao.removeReview(reviewId.toLong())
        return OutputModel("Review with ID = $reviewId has been successfully deleted.")
    }

    private fun getReviewDetails(): Triple<Status, Byte, String> {
        inputManager.showPrompt("Enter your rating for the dish (from 1 to 5): ")
        val rating = inputManager.getInt()
        if (rating < 1 || rating > 5)
            return Triple(
                Status.Failure,
                0,
                "Rating should be in the range from 1 to 5."
            )

        inputManager.showPrompt("Enter your review: ")
        val reviewText = inputManager.getString()
        return Triple(Status.Success, rating.toByte(), reviewText)
    }
}