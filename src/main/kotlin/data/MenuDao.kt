package data

import domain.entity.MenuEntryEntity

interface MenuDao {
    fun getEntryByDishName(name: String) : MenuEntryEntity?
    fun addEntry(menuEntry: MenuEntryEntity)
    fun getAllEntries(): List<MenuEntryEntity>
    fun removeEntry(dishName: String)
    fun updateEntry(updatedEntry: MenuEntryEntity)
}