package di

import data.AccountDao
import data.JsonAccountStorage
import domain.*
import domain.entity.AccountEntity
import domain.entity.AccountType
import java.security.MessageDigest

object DI {
    private const val ACCOUNT_STORAGE_PATH = "src/main/resources/account_storage.json"
    const val SUPERUSER_CODE: String = "SuperUser1337"
    val authenticator: KeyValueAuthenticator<String, String>
        get() = HashAuthenticator(accountDao, hashFunction)

    val inputManager: InputManager
        get() = ConsoleInputManager()

    val authenticationController: AuthenticationController
        get() = AuthenticationControllerImpl(accountDao, authenticator)

    val superuser: AccountEntity by lazy {
        AccountEntity("Admin", "_", AccountType.Administrator)
        // password "_" will never match anything because hash can never be a single character long
    }

    val hashFunction: (str: String) -> String by lazy {
        fun hashFunc(str: String): String {
            val md = MessageDigest.getInstance("SHA-256")
            val hashedBytes = md.digest(str.toByteArray(Charsets.UTF_8))
            val sb = StringBuilder()
            for (b in hashedBytes) {
                sb.append(String.format("%02x", b))
            }
            return sb.toString()
        }
        { str -> hashFunc(str) }
    }

    private val accountDao: AccountDao by lazy {
        JsonAccountStorage(ACCOUNT_STORAGE_PATH)
    }
}