package data

import domain.entity.AccountEntity

interface AccountDao {
    fun getAccount(accountName: String) : AccountEntity?
    fun addAccount(account: AccountEntity)
    fun getAllAccounts(): List<AccountEntity>
}