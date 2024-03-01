package data.dao

import data.dao.interfaces.MenuDao
import data.entity.MenuEntryEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

class JsonMenuStorage(private val jsonMenuStoragePath: String) : MenuDao {

    override fun getEntryByDishName(name: String): MenuEntryEntity? {
        val storedEntries: List<MenuEntryEntity> = getAllEntries()
        return storedEntries.find { entry -> entry.dish.name == name }
    }

    override fun addEntry(menuEntry: MenuEntryEntity) {
        val storedEntries: List<MenuEntryEntity> = getAllEntries()
        if(getEntryByDishName(menuEntry.dish.name) != null)
            return
        
        val updatedEntries = storedEntries.toMutableList()
        updatedEntries.add(menuEntry)
        val serializedUpdatedStorage = Json.encodeToString(updatedEntries.toList())
        writeTextToFile(jsonMenuStoragePath, serializedUpdatedStorage)
    }

    override fun getAllEntries(): List<MenuEntryEntity> {
        val storageFileText = readFileOrCreateEmpty(jsonMenuStoragePath)

        return if (storageFileText.isBlank())
            listOf() else Json.decodeFromString<List<MenuEntryEntity>>(storageFileText)
    }

    override fun removeEntry(dishName: String) {
        val storedEntities: List<MenuEntryEntity> = getAllEntries()
        val updatedEntries = storedEntities.toMutableList()

        updatedEntries.removeIf { oldEntry -> oldEntry.dish.name == dishName }
        val serializedUpdatedStorage = Json.encodeToString(updatedEntries.toList())
        writeTextToFile(jsonMenuStoragePath, serializedUpdatedStorage)
    }

    override fun updateEntry(updatedEntry: MenuEntryEntity) {
        val storedEntities: List<MenuEntryEntity> = getAllEntries()
        if(storedEntities.find { entry -> entry.dish.name == updatedEntry.dish.name } == null)
            return

        val updatedEntries = storedEntities.toMutableList()
        updatedEntries.removeIf { oldEntry -> oldEntry.dish.name == updatedEntry.dish.name }
        updatedEntries.add(updatedEntry)

        val serializedUpdatedStorage = Json.encodeToString(updatedEntries.toList())
        writeTextToFile(jsonMenuStoragePath, serializedUpdatedStorage)
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