package data.dao

import data.dao.interfaces.AccountDao
import data.entity.AccountEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

class JsonAccountStorage(private val jsonAccountStoragePath: String) : AccountDao {
    private val json = Json { prettyPrint = true }

    override fun addAccount(account: AccountEntity) {
        val storageFileText = readFileOrCreateEmpty(jsonAccountStoragePath)
        val storedAccounts: List<AccountEntity> =
            if (storageFileText.isBlank()) listOf()
            else json.decodeFromString(storageFileText)
        val updatedSessions = storedAccounts.toMutableList()

        updatedSessions.removeIf { oldAccount -> oldAccount.name == account.name }
        updatedSessions.add(account)
        val serializedUpdatedStorage = json.encodeToString(updatedSessions.toList())
        writeTextToFile(jsonAccountStoragePath, serializedUpdatedStorage)
    }

    override fun getAccount(accountName: String): AccountEntity? {
        val storageFileText = readFileOrCreateEmpty(jsonAccountStoragePath)
        val storedAccounts: List<AccountEntity> =
            if (storageFileText.isBlank()) listOf() else json.decodeFromString(storageFileText)

        return storedAccounts.find { account -> account.name == accountName }
    }

    override fun getAllAccounts(): List<AccountEntity> {
        val storageFileText = readFileOrCreateEmpty(jsonAccountStoragePath)

        return if (storageFileText.isBlank()) listOf() else json.decodeFromString<List<AccountEntity>>(storageFileText)
    }


    private fun readFileOrCreateEmpty(filePath: String): String {
        val file = File(filePath)
        return try {
            file.readText()
        } catch (exception: FileNotFoundException) {
            file.createNewFile()
            ""
        }
    }

    private fun writeTextToFile(filePath: String, text: String) {
        val file = File(filePath)
        file.writeText(text)
    }
}