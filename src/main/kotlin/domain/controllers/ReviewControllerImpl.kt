package domain.controllers

import data.dao.interfaces.ReviewDao
import data.entity.AccountEntity
import data.entity.OrderEntity
import di.DI
import presentation.model.OutputModel
import presentation.model.Status
import java.time.LocalDateTime

class ReviewControllerImpl(private val reviewDao: ReviewDao) : ReviewController {
    override fun getDishReviews(account: AccountEntity): OutputModel = OutputModel(
        reviewDao
            .getAllReviews()
            .filter { it.accountName == account.name }
            .joinToString(separator = "\n\t", prefix = "Reviews for account \"${account.name}\": \n\t") {
                "Dish: \"${it.dishName}\", Rating: ${it.rating}, Review: ${it.text}"
            }
    )

    override fun leaveReview(account: AccountEntity, order: OrderEntity): OutputModel {
        DI.inputManager.showPrompt("Enter the name of the dish you want to leave a review on: ")
        val dishName = DI.inputManager.getString()

        if (order.dishes.none { it.name == dishName })
            return OutputModel(
                status = Status.Failure,
                message = "Dish \"$dishName\" was not in your order. Cannot leave a review on it."
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
        DI.inputManager.showPrompt("Enter the ID of the review to be changed: ")
        val reviewId = DI.inputManager.getInt()

        val review = reviewDao.getReview(reviewId.toLong())
        if (review == null || review.accountName != account.name) return OutputModel(
            status = Status.Failure,
            message = "No record of review with ID = $reviewId for account \"${account.name}\" found."
        )



        DI.inputManager.showPrompt("Enter new rating for your review: ")

        val reviewInfo = getReviewDetails()
        if (reviewInfo.first == Status.Failure)
            return OutputModel(reviewInfo.third, Status.Failure)

        reviewDao.updateReview(review.copy(text = reviewInfo.third, rating = reviewInfo.second))
        return OutputModel("Your review has been successfully updated.")
    }

    override fun deleteReview(account: AccountEntity): OutputModel {
        DI.inputManager.showPrompt("Enter the ID of the review to be deleted: ")
        val reviewId = DI.inputManager.getInt()
        val review = reviewDao.getReview(reviewId.toLong())
        if (review == null || review.accountName != account.name)
            return OutputModel(
                status = Status.Failure,
                message = "No record of review with ID = $reviewId for account \"${account.name}\" found."
            )
        reviewDao.removeReview(reviewId.toLong())
        return OutputModel("Review with ID = $reviewId has been successfully deleted.")
    }

    private fun getReviewDetails(): Triple<Status, Byte, String> {
        DI.inputManager.showPrompt("Enter your rating for the dish: ")
        val rating = DI.inputManager.getInt()
        if (rating < 1 || rating > 10)
            return Triple(
                Status.Failure,
                0,
                "Unable to create a review: rating should be in the range from 1 to 10."
            )

        DI.inputManager.showPrompt("Enter your review: ")
        val reviewText = DI.inputManager.getString()
        return Triple(Status.Success, rating.toByte(), reviewText)
    }
}