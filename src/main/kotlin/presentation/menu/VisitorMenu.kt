package presentation.menu

import domain.entity.AccountEntity

class VisitorMenu(private val userAccount: AccountEntity) : Menu {
    override fun displayMenu() {
        println("Options:")
        println(
            VisitorMenuOption.entries
                .mapIndexed { index, entry -> "\t${index + 1}. $entry" }
                .joinToString(separator = "\n")
        )
    }

    override fun handleInteractions() {
        displayMenu()
        println("Handling interactions (visitor)...")
    }
}
