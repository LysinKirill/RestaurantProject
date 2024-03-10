package domain.controllers.interfaces

import data.entity.AccountEntity
import presentation.model.OutputModel

interface AuthenticationController {
    fun registerAdminAccount(queryingAccount: AccountEntity?) : Pair<OutputModel, AccountEntity?>

    fun registerVisitorAccount() : Pair<OutputModel, AccountEntity?>

    fun logIntoAdminAccount() : Pair<OutputModel, AccountEntity?>

    fun logIntoVisitorAccount() : Pair<OutputModel, AccountEntity?>

    fun logInAsSuperuser() : Pair<OutputModel, AccountEntity?>
}
