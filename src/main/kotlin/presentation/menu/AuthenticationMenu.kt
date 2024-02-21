package presentation.menu

import di.DI
import domain.AuthenticationController
import domain.entity.AccountEntity
import domain.entity.AccountType
import presentation.model.Status
import kotlin.system.exitProcess

class AuthenticationMenu(private val authenticationController: AuthenticationController) :
    ResponsiveMenu<AccountEntity> {
    private var currentAccount: AccountEntity? = null
    private var isActive = true

    override fun getResponse(): AccountEntity? = currentAccount

    override fun displayMenu() {
        println(
            "Account options:" +
                    "\n\t1. Register as a new visitor" +
                    "\n\t2. Register as a new administrator" +
                    "\n\t3. Log into an account (visitor)" +
                    "\n\t4. Log into an account (administrator)" +
                    "\n\t5. Authorize with the security code" +
                    "\n\t6. Exit"
        )
    }

    override fun handleInteractions() {
        isActive = true
        do {
            println("Current account: ${if (currentAccount == null) "not logged in" else currentAccount?.name}")
            println("Choose one of the following options.")
            displayMenu()
            when (getOption()) {
                AuthenticationMenuOption.RegisterVisitor -> registerVisitor()
                AuthenticationMenuOption.RegisterAdmin -> registerAdmin()
                AuthenticationMenuOption.LoginVisitor -> loginVisitor()
                AuthenticationMenuOption.LoginAdmin -> loginAdmin()
                AuthenticationMenuOption.AuthorizeWithCode -> authorizeWithCode()
                AuthenticationMenuOption.Exit -> {
                    println("Exiting...")
                    exitProcess(0)
                }

                null -> {}

            }

        } while (isActive)
    }

    private fun parseAction(userInput: String): AuthenticationMenuOption? {
        try {
            val optionNumber = userInput.toInt()
            return when (optionNumber) {
                1 -> AuthenticationMenuOption.RegisterVisitor
                2 -> AuthenticationMenuOption.RegisterAdmin
                3 -> AuthenticationMenuOption.LoginVisitor
                4 -> AuthenticationMenuOption.LoginAdmin
                5 -> AuthenticationMenuOption.AuthorizeWithCode
                6 -> AuthenticationMenuOption.Exit
                else -> run {
                    println("Incorrect action chosen...")
                    null
                }
            }
        } catch (ex: Exception) {
            return null
        }
    }

    private fun registerAdmin() {
        if (currentAccount == null || currentAccount?.accountType != AccountType.Administrator) {
            println("Only superuser or users with administrator rights can register admin accounts.")
            return
        }
        val (accountName, password, passwordMatch) = getAccountInfoWithConfirmation()

        if (!passwordMatch) {
            println("Passwords do not match. Aborting account registration...")
            return
        }

        val response = authenticationController.registerAdminAccount(accountName, password)
        println(response.first)
        if (response.first.status == Status.Success && response.second != null) {
            currentAccount = response.second
            isActive = false
            println("Logged in as administrator: ${response.second?.name}")
        }
    }

    private fun registerVisitor() {
        val (accountName, password) = getAccountInfo()

        val response = authenticationController.registerVisitorAccount(accountName, password)
        println(response.first)
        if (response.first.status == Status.Success && response.second != null) {
            currentAccount = response.second
            isActive = false
            println("Logged in as visitor: ${response.second?.name}")
        }
    }

    private fun loginVisitor() {
        val (accountName, password) = getAccountInfo()

        val response = authenticationController.logIntoVisitorAccount(accountName, password)
        if (response.first.status == Status.Success && response.second != null) {
            currentAccount = response.second
            isActive = false
            println("Logged in as visitor: ${response.second?.name}")
        }
        println(response.first)
    }

    private fun loginAdmin() {
        val (accountName, password) = getAccountInfo()

        val response = authenticationController.logIntoAdminAccount(accountName, password)
        if (response.first.status == Status.Success && response.second != null) {
            currentAccount = response.second
            isActive = false
            println("Logged in as administrator: ${response.second?.name}")
        }
        println(response.first)
    }

    private fun authorizeWithCode() {
        DI.inputManager.showPrompt("Enter the security code of the superuser: ")
        val superUserCode = DI.inputManager.getString()
        if (superUserCode == DI.SUPERUSER_CODE) {
            currentAccount = DI.superuser
            isActive = false
            println("Logged in as Admin.")
            return
        }
        println("The provided security code does not match the security code of the superuser. Could not log into an account.")
    }

    private fun getAccountInfo(): Pair<String, String> {
        DI.inputManager.showPrompt("Enter the name of the account: ")
        val accountName = DI.inputManager.getString()

        DI.inputManager.showPrompt("Enter the password: ")
        val password = DI.inputManager.getString()

        return Pair(accountName, password)
    }

    private fun getAccountInfoWithConfirmation(): Triple<String, String, Boolean> {
        val (accountName, password) = getAccountInfo()
        DI.inputManager.showPrompt("Enter the password again: ")
        val passwordConfirmation = DI.inputManager.getString()

        return Triple(accountName, password, password == passwordConfirmation)
    }

    private fun getOption(): AuthenticationMenuOption? {
        return readlnOrNull()?.let { parseAction(it) }
    }
}
