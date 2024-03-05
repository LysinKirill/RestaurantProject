package presentation.menu

import presentation.menu.options.VisitorMenuOption

class VisitorMenu(
    private val reviewMenu: ReviewMenu,
    private val orderMenu: OrderMenu,
) : Menu {
    override fun displayMenu() {
        println("Options:")
        println(
            VisitorMenuOption.entries
                .mapIndexed { index, entry -> "\t${index + 1}. $entry" }
                .joinToString(separator = "\n")
        )
    }

    override fun handleInteractions() {
        var isActive = true
        do {
            println("Choose one of the following options.")
            displayMenu()
            when (getOption()) {
                VisitorMenuOption.OpenOrderMenu -> orderMenu.handleInteractions()
                VisitorMenuOption.OpenReviewMenu -> reviewMenu.handleInteractions()
                VisitorMenuOption.LogOut -> {
                    println("Logging out...")
                    isActive = false
                }

                null -> {}
            }

        } while (isActive)
    }

    private fun getOption(): VisitorMenuOption? {
        return readlnOrNull()?.let { parseAction(it) }
    }

    private fun parseAction(userInput: String): VisitorMenuOption? {
        try {
            val optionNumber = userInput.toInt() - 1
            if (optionNumber >= VisitorMenuOption.entries.size || optionNumber < 0) {
                println("Incorrect action chosen...")
                return null
            }
            return VisitorMenuOption.entries[optionNumber]
        } catch (ex: Exception) {
            return null
        }
    }
}
