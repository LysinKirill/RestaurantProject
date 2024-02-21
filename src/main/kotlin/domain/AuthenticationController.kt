package domain

import domain.entity.AccountEntity
import presentation.model.OutputModel

interface AuthenticationController {
    fun registerAdminAccount(name: String, password: String) : Pair<OutputModel, AccountEntity?>

    fun registerVisitorAccount(name: String, password: String) : Pair<OutputModel, AccountEntity?>

    fun logIntoAdminAccount(name: String, password: String) : Pair<OutputModel, AccountEntity?>

    fun logIntoVisitorAccount(name: String, password: String) : Pair<OutputModel, AccountEntity?>

    fun logInAsSuperuser(securityCode: String) : Pair<OutputModel, AccountEntity?>
}
