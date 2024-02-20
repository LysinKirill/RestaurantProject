package domain

import domain.entity.Account
import presentation.model.OutputModel

class AuthenticationController {
    fun registerAdminAccount(name: String, password: String) : Pair<OutputModel, Account?> {
        TODO()
    }

    fun registerVisitorAccount(name: String, password: String) : Pair<OutputModel, Account?> {
        TODO()
    }

    fun LogIntoAdminAccount(name: String, password: String) : Pair<OutputModel, Account?> {
        TODO()
    }

    fun LogIntoVisitorAccount(name: String, password: String) : Pair<OutputModel, Account?> {
        TODO()
    }

    fun CheckSecurityToken(token: String): Boolean = TODO()

}