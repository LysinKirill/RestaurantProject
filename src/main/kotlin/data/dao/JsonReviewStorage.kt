package data.dao

import data.dao.interfaces.ReviewDao
import data.entity.ReviewEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDateTime

class JsonReviewStorage(
    private val jsonReviewStoragePath: String
) : ReviewDao {
    private val json = Json { prettyPrint = true }
    override fun addReview(
        accountName: String,
        dishName: String,
        text: String,
        rating: Byte,
        timeStamp: LocalDateTime,
    ) {
        val storedReviews = getAllReviews().toMutableList()
        val newId = if (storedReviews.isEmpty()) 1 else storedReviews.maxOf { order -> order.id } + 1

        val newReview = ReviewEntity(
            id = newId,
            accountName = accountName,
            dishName = dishName,
            text = text,
            rating = rating,
            timeStamp = timeStamp,
        )
        storedReviews.add(newReview)
        writeTextToFile(jsonReviewStoragePath, json.encodeToString(storedReviews.toList()))
    }

    override fun getReview(reviewId: Long) = getAllReviews().find { it.id == reviewId }

    override fun removeReview(reviewId: Long) = writeTextToFile(
        filePath = jsonReviewStoragePath,
        text = json.encodeToString(getAllReviews().filterNot { it.id == reviewId })
    )

    override fun getAllReviews(): List<ReviewEntity> {
        val storageFileText = readFileOrCreateEmpty(jsonReviewStoragePath)
        return if (storageFileText.isBlank())
            listOf() else json.decodeFromString<List<ReviewEntity>>(storageFileText)
    }

    override fun updateReview(updatedReview: ReviewEntity) {
        val storedReviews = getAllReviews().toMutableList()
        if (!storedReviews.removeIf { it.id == updatedReview.id }) {
            // Nothing to update. Order with this id not found
            return
        }
        storedReviews.add(updatedReview)
        writeTextToFile(jsonReviewStoragePath, json.encodeToString(storedReviews.toList()))
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