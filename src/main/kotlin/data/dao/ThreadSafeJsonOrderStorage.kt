package data.dao

import data.dao.interfaces.OrderDao
import data.entity.DishEntity
import data.entity.OrderEntity
import data.entity.OrderStatus
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDateTime


class ThreadSafeJsonOrderStorage(private val jsonOrderStoragePath: String) : OrderDao {

    private val lock = Any()

    override fun addOrder(
        visitorAccountName: String,
        finishTime: LocalDateTime,
        dishes: List<DishEntity>
    ): OrderEntity {
        synchronized(lock) {
            val storedOrders = readOrdersFromJsonFile().toMutableList()
            val newId = if (storedOrders.isEmpty()) 1 else storedOrders.maxOf { order -> order.id } + 1

            val newOrder = OrderEntity(
                id = newId,
                visitorAccountName = visitorAccountName,
                finishTime = finishTime,
                dishes = dishes,
                status = OrderStatus.Created
            )
            storedOrders.add(newOrder)
            writeOrdersToFile(storedOrders)
            return newOrder
        }
    }

    override fun getOrder(orderId: Int): OrderEntity? {
        synchronized(lock) {
            return readOrdersFromJsonFile().find { order -> order.id == orderId }
        }
    }

    override fun removeOrder(orderId: Int) {
        synchronized(lock) {
            val storedOrders = readOrdersFromJsonFile().toMutableList()
            storedOrders.removeIf { oldOrder -> oldOrder.id == orderId }
            writeOrdersToFile(storedOrders)
        }
    }

    override fun getAllOrders(): List<OrderEntity> {
        synchronized(lock) {
            val storageFileText = readFileOrCreateEmpty(jsonOrderStoragePath)
            return if (storageFileText.isBlank())
                listOf() else Json.decodeFromString<List<OrderEntity>>(storageFileText)
        }
    }

    override fun updateOrder(updatedOrder: OrderEntity) {
        synchronized(lock) {
            val storedOrders = readOrdersFromJsonFile().toMutableList()
            if (!storedOrders.removeIf { order -> order.id == updatedOrder.id }) {
                // Nothing to update. Order with this id not found
                return
            }
            storedOrders.add(updatedOrder)
            writeOrdersToFile(storedOrders)
        }
    }

    private fun readOrdersFromJsonFile(): List<OrderEntity> {
        val storageFileText = readFileOrCreateEmpty(jsonOrderStoragePath)
        return if (storageFileText.isBlank())
            listOf() else Json.decodeFromString<List<OrderEntity>>(storageFileText)
    }

    private fun writeOrdersToFile(orders: List<OrderEntity>) {
        val serializedOrders = Json.encodeToString(orders)
        writeTextToFile(jsonOrderStoragePath, serializedOrders)
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
