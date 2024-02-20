package presentation.model

data class OutputModel(
    val message: String,
    val status: Status = Status.Success
) {
    override fun toString(): String {
        return message
    }
}