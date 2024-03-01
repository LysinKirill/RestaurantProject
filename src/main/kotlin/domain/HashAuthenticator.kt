package domain

import data.dao.interfaces.AccountDao

class HashAuthenticator(
    private val accountDao: AccountDao,
    private val hashFunction: (String) -> String
) : KeyValueAuthenticator<String, String> {
    override fun verify(key: String, value: String): Boolean {
        val account = accountDao.getAccount(key) ?: return false
        return account.hashedPassword == hashFunction(value)
    }
}