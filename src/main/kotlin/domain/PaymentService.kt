package domain

import data.entity.AccountEntity

interface PaymentService {
    fun receivePayment(account: AccountEntity, paymentAmount: Double) : Boolean
    fun requestPayment(requestPrompt: String)
}