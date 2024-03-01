package domain.controllers

import data.entity.AccountEntity
import data.entity.OrderEntity
import presentation.model.OutputModel

interface ReviewController {
    fun getDishReviews(account: AccountEntity): OutputModel
    fun leaveReview(account: AccountEntity, order: OrderEntity): OutputModel
    fun editReview(account: AccountEntity): OutputModel
    fun deleteReview(account: AccountEntity): OutputModel
}