package data.dao.interfaces

import data.entity.ReviewEntity


interface ReviewDao {
    fun addReview(dishName: String, text: String, rating: Byte) // change method signature to addReview(review: ReviewEntity)
    fun getReview(reviewId: Long) : ReviewEntity?
    fun removeReview(reviewId: Long)
    fun getAllReviews() : List<ReviewEntity>
    fun updateReview(updatedReview: ReviewEntity)
}