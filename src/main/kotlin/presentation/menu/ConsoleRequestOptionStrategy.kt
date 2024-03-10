package presentation.menu

import presentation.menu.interfaces.RequestOptionStrategy


class ConsoleRequestOptionStrategy<T : Enum<T>>(
    private val enumClass: Class<T>,
) : RequestOptionStrategy<T> {
    override fun requestOption(): T? {
        return readlnOrNull()?.let { parseAction(it) }
    }

    private fun parseAction(userInput: String): T? {
        try {
            val enumValues = enumClass.enumConstants
            val optionNumber = userInput.toInt() - 1
            if (optionNumber >= enumValues.size || optionNumber < 0) {
                println("Incorrect action chosen...")
                return null
            }
            return enumValues[optionNumber]
        } catch (ex: Exception) {
            return null
        }
    }
}