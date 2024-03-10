package domain.controllers

import data.dao.interfaces.MenuDao
import data.entity.DishEntity
import data.entity.MenuEntryEntity
import domain.InputManager
import domain.controllers.interfaces.RestaurantMenuController
import presentation.model.OutputModel
import presentation.model.Status

class RestaurantMenuControllerImpl(
    private val menuDao: MenuDao,
    private val inputManager: InputManager,
) : RestaurantMenuController {
    override fun addMenuEntry(): OutputModel {
        val getDishResponse = getNewDish()
        val dish = getDishResponse.second
        if (getDishResponse.first.status == Status.Failure || dish == null)
            return createFailureResponse("Failed to add new entry to menu.\n" + getDishResponse.first.message)

        inputManager.showPrompt("Enter the amount of dishes: ")
        val dishNumber = inputManager.getInt()
        if (dishNumber <= 0)
            return createFailureResponse("Failed to add new entry to menu.\nNumber of dishes should be a positive integer.")

        menuDao.addEntry(MenuEntryEntity(dish, dishNumber))
        return OutputModel("Added new dish \"${dish.name}\" to menu.")
    }

    override fun removeMenuEntry(): OutputModel {
        inputManager.showPrompt("Enter the name of the dish to be removed from the menu: ")
        val dishToRemoveName = inputManager.getString()

        if (menuDao.getEntryByDishName(dishToRemoveName) == null)
            return createFailureResponse("Dish with name $dishToRemoveName is not on the menu.")

        menuDao.removeEntry(dishToRemoveName)
        return OutputModel("Entry for dish \"$dishToRemoveName\" was removed from the menu.")
    }

    override fun changeDishPrice(): OutputModel {
        inputManager.showPrompt("Enter the name of the dish which price will be changed: ")
        val dishName = inputManager.getString()

        val menuEntry = menuDao.getEntryByDishName(dishName)
            ?: return createFailureResponse("Dish with name $dishName is not on the menu.")

        inputManager.showPrompt("Enter new price for the dish \"$dishName\": ")
        val newPrice = inputManager.getDouble()
        if (newPrice <= 0)
            return createFailureResponse("Failed to change dish price.\nPrice of dish should be a positive real number.")

        val updatedEntry = menuEntry.copy(
            dish = menuEntry.dish.copy(
                price = newPrice
            )
        )
        menuDao.updateEntry(updatedEntry)
        return OutputModel("The price of dish \"$dishName\" was changed to $newPrice.")
    }

    override fun changeDishCount(): OutputModel {
        inputManager.showPrompt("Enter the name of the dish which amount will be changed: ")
        val dishName = inputManager.getString()

        val menuEntry = menuDao.getEntryByDishName(dishName)
            ?: return createFailureResponse("Dish with name $dishName is not on the menu.")

        inputManager.showPrompt("Enter new amount for the dish \"$dishName\": ")
        val newAmount = inputManager.getInt()
        if (newAmount < 0)
            return createFailureResponse("Failed to change dish amount.\nAmount of dish should be a non-negative integer.")

        val updatedEntry = menuEntry.copy(remainingNumber = newAmount)
        menuDao.updateEntry(updatedEntry)
        return OutputModel("The amount of dish \"$dishName\" was changed to $newAmount.")
    }

    override fun changeDishCookingTime(): OutputModel {
        inputManager.showPrompt("Enter the name of the dish which cooking time will be changed: ")
        val dishName = inputManager.getString()

        val menuEntry = menuDao.getEntryByDishName(dishName)
            ?: return createFailureResponse("Dish with name $dishName is not on the menu.")

        inputManager.showPrompt("Enter new cooking time (in seconds) for the dish \"$dishName\": ")
        val newCookingTimeInSeconds = inputManager.getInt()
        if (newCookingTimeInSeconds <= 0)
            return createFailureResponse(
                "Failed to change dish cooking time." +
                        "\nCooking time of dish should be a positive integer representing the number of seconds required to cook the dish."
            )

        val updatedEntry = menuEntry.copy(
            dish = menuEntry.dish.copy(
                cookingTimeInSeconds = newCookingTimeInSeconds
            )
        )
        menuDao.updateEntry(updatedEntry)
        return OutputModel("The cooking time of dish \"$dishName\" was changed to $newCookingTimeInSeconds.")
    }

    override fun getAllMenuEntries(): OutputModel {
        return OutputModel(
            menuDao.getAllEntries()
                .joinToString(separator = "\n\t", prefix = "Menu entries:\n\t") { entry ->
                    "${entry.dish}; Remaining dishes: ${entry.remainingNumber}"
                }
        )
    }

    override fun getAvailableDishes(): OutputModel {
        val menuEntries = menuDao.getAllEntries()
        if (menuEntries.isEmpty())
            return createFailureResponse("No dishes available.")

        val longestNameLength = menuEntries.maxOf { it.dish.name.length }
        val longestDishPrice = menuEntries.maxOf { it.dish.price }.toString().length
        val longestCookingTime = menuEntries.maxOf { it.dish.cookingTimeInSeconds }.toString().length

        return OutputModel(
            menuEntries
                .filter { entry -> entry.remainingNumber > 0 }
                .joinToString(separator = "\n\t", prefix = "Menu entries:\n\t") { entry ->
                    "Name: ${entry.dish.name}".padEnd(longestNameLength + 10) +
                            "Price: ${entry.dish.price}".padEnd(longestDishPrice + 11) +
                            "Cooking time ${entry.dish.cookingTimeInSeconds}".padEnd(longestCookingTime + 18) +
                            "Remaining dishes: ${entry.remainingNumber}"
                }
        )
    }

    private fun getNewDish(): Pair<OutputModel, DishEntity?> {
        inputManager.showPrompt("Enter the name of the new dish: ")
        val dishName = inputManager.getString()
        if (dishName.isBlank())
            return createFailurePairResponse("Dish name cannot be blank.")

        inputManager.showPrompt("Enter the price of the new dish: ")
        val dishPrice = inputManager.getDouble()
        if (dishPrice <= 0)
            return createFailurePairResponse("Dish price should be a positive real number.")

        inputManager.showPrompt("Enter the cooking time of the new dish in seconds: ")
        val dishCookingTime = inputManager.getInt()
        if (dishCookingTime <= 0)
            return createFailurePairResponse("Dish cooking time should be a positive integer.")


        return createSuccessPairResponse(
            "Menu entry for dish $dishName was created.",
            DishEntity(dishName, dishPrice, dishCookingTime)
        )
    }

    private fun <T> createFailurePairResponse(message: String): Pair<OutputModel, T?> =
        Pair(
            OutputModel(
                message = message,
                status = Status.Failure
            ), null
        )

    private fun <T> createSuccessPairResponse(message: String, response: T): Pair<OutputModel, T?> =
        Pair(
            OutputModel(message = message),
            response
        )

    private fun createFailureResponse(message: String): OutputModel =
        OutputModel(
            message = message,
            status = Status.Failure
        )
}