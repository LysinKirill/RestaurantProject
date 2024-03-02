package domain.controllers

import data.dao.interfaces.AccountDao
import domain.KeyValueAuthenticator
import data.entity.AccountEntity
import data.entity.AccountType
import di.DI
import domain.InputManager
import presentation.model.OutputModel
import presentation.model.Status

class AuthenticationControllerImpl(
    private val accountDao: AccountDao,
    private val authenticator: KeyValueAuthenticator<String, String>,
    private val inputManager: InputManager,
    private val hashFunction: (String) -> String,
) : AuthenticationController {
    override fun registerAdminAccount(queryingAccount: AccountEntity?): Pair<OutputModel, AccountEntity?> {
        if (queryingAccount == null || queryingAccount.accountType != AccountType.Administrator)
            return createFailureResponse("Failed to register a new account.\nOnly superuser or users with administrator rights can register admin accounts.")

        val (name, password, passwordMatch) = getAccountInfoWithConfirmation()
        if (!passwordMatch)
            return createFailureResponse("Failed to register a new account.\nPasswords do not match.")

        val accountValidation = checkAccountInfo(name, password)
        if (accountValidation.status != Status.Success)
            return createFailureResponse("Failed to register a new account.\n" + accountValidation.message)

        val hashedPassword = hashFunction(password)
        val newAdminAccount = AccountEntity(name, hashedPassword, AccountType.Administrator)
        accountDao.addAccount(newAdminAccount)
        return createSuccessResponse(
            "An account with administrator rights by the name of $name has been created.",
            newAdminAccount
        )
    }

    override fun registerVisitorAccount(): Pair<OutputModel, AccountEntity?> {
        val (name, password) = getAccountInfo()
        val accountValidation = checkAccountInfo(name, password)
        if (accountValidation.status != Status.Success)
            return createFailureResponse("Failed to register new account.\n" + accountValidation.message)

        val hashedPassword = hashFunction(password)
        val newVisitorAccount = AccountEntity(name, hashedPassword, AccountType.Visitor)
        accountDao.addAccount(newVisitorAccount)
        return createSuccessResponse(
            "A visitor account by the name of $name has been created.",
            newVisitorAccount
        )
    }

    override fun logIntoAdminAccount(): Pair<OutputModel, AccountEntity?> {
        val (name, password) = getAccountInfo()
        val account = accountDao.getAccount(name)

        if (account == null || account.accountType != AccountType.Administrator)
            return createFailureResponse("Failed to log into the account.\nAdministrator account with the name $name does not exist.")

        if (!authenticator.verify(name, password))
            return createFailureResponse("Failed to log into the account.\nIncorrect password for account name $name.")

        return createSuccessResponse("Logged into account $name with administrator rights.", account)
    }

    override fun logIntoVisitorAccount(): Pair<OutputModel, AccountEntity?> {
        val (name, password) = getAccountInfo()
        val account = accountDao.getAccount(name)

        if (account == null || account.accountType != AccountType.Visitor)
            return createFailureResponse("Failed to log into the account.\nVisitor account with the name $name does not exist.")

        if (!authenticator.verify(name, password))
            return createFailureResponse("Failed to log into the account.\nIncorrect password for account name $name.")

        return createSuccessResponse("Logged into account visitor account $name.", account)
    }

    override fun logInAsSuperuser(): Pair<OutputModel, AccountEntity?> {
        inputManager.showPrompt("Enter the security code of the superuser: ")
        val securityCode = inputManager.getString()

        if (securityCode != DI.SUPERUSER_CODE)
            return createFailureResponse("Failed to log in as superuser. Security code does not match.")

        return createSuccessResponse("Logged into the superuser account.", DI.superuser)
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


    private fun getAccountInfo(): Pair<String, String> {
        inputManager.showPrompt("Enter the name of the account: ")
        val accountName = inputManager.getString()

        inputManager.showPrompt("Enter the password: ")
        val password = inputManager.getString()

        return Pair(accountName, password)
    }

    private fun getAccountInfoWithConfirmation(): Triple<String, String, Boolean> {
        val (accountName, password) = getAccountInfo()
        inputManager.showPrompt("Enter the password again: ")
        val passwordConfirmation = inputManager.getString()

        return Triple(accountName, password, password == passwordConfirmation)
    }

    private fun createFailureResponse(message: String): Pair<OutputModel, AccountEntity?> =
        Pair(
            OutputModel(
                message = message,
                status = Status.Failure
            ), null
        )

    private fun createSuccessResponse(message: String, account: AccountEntity): Pair<OutputModel, AccountEntity?> =
        Pair(
            OutputModel(message = message),
            account
        )
}