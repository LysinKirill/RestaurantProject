package data

import data.entity.RestaurantStatisticsEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

class RestaurantStatisticsStorage(
    private val jsonRestaurantStatisticsPath: String
) : RestaurantStatisticsDao {
    override fun getStatistics() : RestaurantStatisticsEntity {
        val storageFileText = readFileOrCreateEmpty(jsonRestaurantStatisticsPath)
        if(storageFileText.isBlank())
            return RestaurantStatisticsEntity(0.0)
        return Json.decodeFromString<RestaurantStatisticsEntity>(storageFileText)
    }

    override fun saveStatistics(statistics: RestaurantStatisticsEntity) {
        val serializedStatistics = Json.encodeToString(statistics)
        writeTextToFile(jsonRestaurantStatisticsPath, serializedStatistics)
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