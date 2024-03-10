package domain.controllers.interfaces

import data.entity.AccountEntity
import presentation.model.OutputModel

interface ReviewController {
    fun getDishReviews(account: AccountEntity): OutputModel
    fun leaveReview(account: AccountEntity): OutputModel
    fun editReview(account: AccountEntity): OutputModel
    fun deleteReview(account: AccountEntity): OutputModel
}