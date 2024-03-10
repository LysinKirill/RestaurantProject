package domain.controllers.interfaces

import presentation.model.OutputModel

interface RestaurantMenuController {
    fun addMenuEntry() : OutputModel
    fun removeMenuEntry() : OutputModel
    fun changeDishPrice() : OutputModel
    fun changeDishCount() : OutputModel
    fun changeDishCookingTime() : OutputModel
    fun getAllMenuEntries() : OutputModel
    fun getAvailableDishes() : OutputModel
}