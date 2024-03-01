package data.dao.interfaces

import data.entity.MenuEntryEntity

interface MenuDao {
    fun getEntryByDishName(name: String) : MenuEntryEntity?
    fun addEntry(menuEntry: MenuEntryEntity)
    fun getAllEntries(): List<MenuEntryEntity>
    //fun getAvailableEntries(): List<Menu>
    fun removeEntry(dishName: String)
    fun updateEntry(updatedEntry: MenuEntryEntity)
}