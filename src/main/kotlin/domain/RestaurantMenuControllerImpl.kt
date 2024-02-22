package domain

import data.MenuDao
import di.DI
import domain.entity.DishEntity
import domain.entity.MenuEntryEntity
import presentation.model.OutputModel
import presentation.model.Status

class RestaurantMenuControllerImpl(private val menuDao: MenuDao) : RestaurantMenuController {
    override fun addMenuEntry(): OutputModel {
        val getDishResponse = getNewDish()
        val dish = getDishResponse.second
        if (getDishResponse.first.status == Status.Failure || dish == null)
            return createFailureResponse("Failed to add new entry to menu.\n" + getDishResponse.first.message)

        DI.inputManager.showPrompt("Enter the amount of dishes: ")
        val dishNumber = DI.inputManager.getInt()
        if (dishNumber <= 0)
            return createFailureResponse("Failed to add new entry to menu.\nNumber of dishes should be a positive integer.")

        menuDao.addEntry(MenuEntryEntity(dish, dishNumber))
        return OutputModel("Added new dish \"${dish.name}\" to menu.")
    }

    override fun removeMenuEntry(): OutputModel {
        DI.inputManager.showPrompt("Enter the name of the dish to be removed from the menu: ")
        val dishToRemoveName = DI.inputManager.getString()

        if (menuDao.getEntryByDishName(dishToRemoveName) == null)
            return createFailureResponse("Dish with name $dishToRemoveName is not on the menu.")

        menuDao.removeEntry(dishToRemoveName)
        return OutputModel("Entry for dish \"$dishToRemoveName\" was removed from the menu.")
    }

    override fun changeDishPrice(): OutputModel {
        DI.inputManager.showPrompt("Enter the name of the dish which price will be changed: ")
        val dishName = DI.inputManager.getString()

        val menuEntry = menuDao.getEntryByDishName(dishName)
            ?: return createFailureResponse("Dish with name $dishName is not on the menu.")

        DI.inputManager.showPrompt("Enter new price for the dish \"$dishName\": ")
        val newPrice = DI.inputManager.getDouble()
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
        DI.inputManager.showPrompt("Enter the name of the dish which amount will be changed: ")
        val dishName = DI.inputManager.getString()

        val menuEntry = menuDao.getEntryByDishName(dishName)
            ?: return createFailureResponse("Dish with name $dishName is not on the menu.")

        DI.inputManager.showPrompt("Enter new amount for the dish \"$dishName\": ")
        val newAmount = DI.inputManager.getInt()
        if (newAmount < 0)
            return createFailureResponse("Failed to change dish amount.\nAmount of dish should be a non-negative integer.")

        val updatedEntry = menuEntry.copy(remainingNumber = newAmount)
        menuDao.updateEntry(updatedEntry)
        return OutputModel("The amount of dish \"$dishName\" was changed to $newAmount.")
    }

    override fun changeDishCookingTime(): OutputModel {
        DI.inputManager.showPrompt("Enter the name of the dish which cooking time will be changed: ")
        val dishName = DI.inputManager.getString()

        val menuEntry = menuDao.getEntryByDishName(dishName)
            ?: return createFailureResponse("Dish with name $dishName is not on the menu.")

        DI.inputManager.showPrompt("Enter new cooking time (in seconds) for the dish \"$dishName\": ")
        val newCookingTimeInSeconds = DI.inputManager.getInt()
        if (newCookingTimeInSeconds <= 0)
            return createFailureResponse("Failed to change dish cooking time." +
                    "\nCooking time of dish should be a positive integer representing the number of seconds required to cook the dish.")

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
                .joinToString(separator = "\n\t", prefix = "Menu entries:\n\t"){
                    entry -> "${entry.dish}; Remaining dishes: ${entry.remainingNumber}"
                }
        )
    }

    override fun getAvailableDishes(): OutputModel {
        return OutputModel(
            menuDao.getAllEntries()
                .filter { entry -> entry.remainingNumber > 0 }
                .joinToString(separator = "\n\t", prefix = "Menu entries:\n\t"){
                        entry -> "${entry.dish}; Remaining dishes: ${entry.remainingNumber}"
                }
        )
    }

    private fun getNewDish(): Pair<OutputModel, DishEntity?> {
        DI.inputManager.showPrompt("Enter the name of the new dish: ")
        val dishName = DI.inputManager.getString()
        if (dishName.isBlank())
            return createFailurePairResponse("Dish name cannot be blank.")

        DI.inputManager.showPrompt("Enter the price of the new dish: ")
        val dishPrice = DI.inputManager.getDouble()
        if (dishPrice <= 0)
            return createFailurePairResponse("Dish price should be a positive real number.")

        DI.inputManager.showPrompt("Enter the cooking time of the new dish in seconds: ")
        val dishCookingTime = DI.inputManager.getInt()
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