package presentation.menu

import presentation.menu.interfaces.DisplayStrategy


class DefaultDisplayStrategy<T : Enum<T>>(
    private val enumClass: Class<T>,
    private val prompt: String = "Options"
) : DisplayStrategy {
    override fun display() {
        val enumValues = enumClass.enumConstants
        println("$prompt:")
        println(
            enumValues
                .mapIndexed { index, entry -> "\t${index + 1}. ${camelCaseToText(entry.toString())}" }
                .joinToString(separator = "\n")
        )
    }

    private fun camelCaseToText(camelCaseString: String): String {
        if (camelCaseString.isEmpty())
            return ""
        val sb = StringBuilder()
        sb.append(camelCaseString[0].uppercase())

        for (index in 1..<camelCaseString.length) {
            if (!camelCaseString[index].isUpperCase()) {
                sb.append(camelCaseString[index])
                continue
            }
            sb.append(' ')
            sb.append(camelCaseString[index].lowercase())
        }
        return sb.toString()
    }
}