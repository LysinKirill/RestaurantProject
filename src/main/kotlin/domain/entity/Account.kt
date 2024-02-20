package domain.entity
import kotlinx.serialization.Serializable

@Serializable
data class Account(val name: String, val hashedPassword: String, val accountType: AccountType)
