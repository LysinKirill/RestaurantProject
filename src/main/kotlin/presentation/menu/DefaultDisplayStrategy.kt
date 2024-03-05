package presentation.menu


class DefaultDisplayStrategy<T : Enum<T>>(
    private val enumClass: Class<T>,
    private val prompt: String = "Options"
) : DisplayStrategy {
    override fun display() {
        val enumValues = enumClass.enumConstants
        println("$prompt:")
        println(
            enumValues
                .mapIndexed { index, entry -> "\t${index + 1}. $entry" }
                .joinToString(separator = "\n")
        )
    }
}