package data.dao.interfaces

import data.entity.ReviewEntity
import java.time.LocalDateTime


interface ReviewDao {
    fun addReview(
        accountName: String,
        dishName: String,
        text: String,
        rating: Byte,
        timeStamp: LocalDateTime,
    ) // change method signature to addReview(review: ReviewEntity)
    fun getReview(reviewId: Long) : ReviewEntity?
    fun removeReview(reviewId: Long)
    fun getAllReviews() : List<ReviewEntity>
    fun updateReview(updatedReview: ReviewEntity)
}