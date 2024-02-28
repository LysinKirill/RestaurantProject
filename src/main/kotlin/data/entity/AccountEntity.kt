package data.entity
import kotlinx.serialization.Serializable

@Serializable
data class AccountEntity(
    val name: String,
    val hashedPassword: String,
    val accountType: AccountType
)
