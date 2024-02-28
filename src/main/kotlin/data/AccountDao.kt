package data

import data.entity.AccountEntity

interface AccountDao {
    fun getAccount(accountName: String) : AccountEntity?
    fun addAccount(account: AccountEntity)
    fun getAllAccounts(): List<AccountEntity>
}