package domain

import data.AccountDao
import di.DI
import domain.entity.AccountEntity
import domain.entity.AccountType
import presentation.model.OutputModel
import presentation.model.Status

class AuthenticationControllerImpl(
    private val accountDao: AccountDao,
    private val authenticator: KeyValueAuthenticator<String, String>
) : AuthenticationController {
    override fun registerAdminAccount(name: String, password: String): Pair<OutputModel, AccountEntity?> {
        val accountValidation = checkAccountInfo(name, password)
        if (accountValidation.status != Status.Success)
            return Pair(
                accountValidation.copy(
                    message = "Failed to register new account.\n" + accountValidation.message
                ),
                null
            )

        val hashedPassword = DI.hashFunction(password)
        val newAdminAccount = AccountEntity(name, hashedPassword, AccountType.Administrator)
        accountDao.addAccount(newAdminAccount)
        return Pair(
            OutputModel("An account with administrator rights by the name of $name has been created."),
            newAdminAccount
        )
    }

    override fun registerVisitorAccount(name: String, password: String): Pair<OutputModel, AccountEntity?> {
        val accountValidation = checkAccountInfo(name, password)
        if (accountValidation.status != Status.Success)
            return Pair(
                accountValidation.copy(
                    message = "Failed to register new account.\n" + accountValidation.message
                ),
                null
            )

        val hashedPassword = DI.hashFunction(password)
        val newVisitorAccount = AccountEntity(name, hashedPassword, AccountType.Visitor)
        accountDao.addAccount(newVisitorAccount)
        return Pair(OutputModel("A visitor account by the name of $name has been created."), newVisitorAccount)
    }

    override fun logIntoAdminAccount(name: String, password: String): Pair<OutputModel, AccountEntity?> {
        val account = accountDao.getAccount(name)
        if (account == null || account.accountType != AccountType.Administrator)
            return Pair(
                OutputModel(
                    message = "Failed to log into the account.\nAdministrator account with the name $name does not exist.",
                    status = Status.Failure
                ),
                null
            )

        if (!authenticator.verify(name, password))
            return Pair(
                OutputModel(
                    message = "Failed to log into the account.\nIncorrect password for account name $name.",
                    status = Status.Failure
                ),
                null
            )
        return Pair(OutputModel("Logged into account $name with administrator rights."), account)
    }

    override fun logIntoVisitorAccount(name: String, password: String): Pair<OutputModel, AccountEntity?> {
        val account = accountDao.getAccount(name)
        if (account == null || account.accountType != AccountType.Visitor)
            return Pair(
                OutputModel(
                    message = "Failed to log into the account.\nVisitor account with the name $name does not exist.",
                    status = Status.Failure
                ),
                null
            )

        if (!authenticator.verify(name, password))
            return Pair(
                OutputModel(
                    message = "Failed to log into the account.\nIncorrect password for account name $name.",
                    status = Status.Failure
                ),
                null
            )
        return Pair(OutputModel("Logged into account visitor account $name."), account)
    }

    override fun logInAsSuperuser(securityCode: String): Pair<OutputModel, AccountEntity?> {
        if (securityCode != DI.SUPERUSER_CODE)
            return Pair(
                OutputModel(
                    message = "Failed to log in as superuser. Security code does not match.",
                    status = Status.Failure
                ),
                null
            )
        return Pair(OutputModel("Logged into the superuser account."), DI.superuser)
    }

    private fun checkAccountName(accountName: String): OutputModel {
        if (accountName.lowercase() == DI.superuser.name.lowercase())
            return OutputModel(
                message = "Invalid name for account: $accountName. The name of account cannot resemble the name of superuser account - ${DI.superuser.name}",
                status = Status.Failure
            )

        if (accountDao.getAllAccounts().find { account -> account.name == accountName } != null)
            return OutputModel(
                message = "Invalid name for account: $accountName. An account with the same name already exists.",
                status = Status.Failure
            )

        if (accountName.isEmpty())
            return OutputModel(
                message = "An account with empty name cannot be created.",
                status = Status.Failure
            )

        return OutputModel(message = "Valid name.")
    }

    private fun checkPassword(password: String): OutputModel {
        if (password.isBlank())
            return OutputModel(
                message = "Password cannot be blank.",
                status = Status.Failure
            )
        return OutputModel(message = "Valid password.")
    }

    private fun checkAccountInfo(name: String, password: String): OutputModel {
        if (checkAccountName(name).status != Status.Success)
            return checkAccountName(name)
        if (checkPassword(password).status != Status.Success)
            return checkPassword(password)
        return OutputModel("Valid account info.")
    }
}