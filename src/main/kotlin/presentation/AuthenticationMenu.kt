package presentation

import di.DI
import domain.AuthenticationController
import domain.entity.Account
import presentation.model.Status
import kotlin.system.exitProcess

class AuthenticationMenu(private val authenticationController: AuthenticationController) :
    ResponsiveMenu<Account> {
    private var currentAccount: Account? = null;
    private var isActive = true

    override fun getResponse(): Account? = currentAccount

    override fun displayMenu() {
        println(
            "Account options:" +
                    "\n\t1. Register as a new visitor" +
                    "\n\t2. Register as a new administrator" +
                    "\n\t3. Log into an account (visitor)" +
                    "\n\t4. Log into an account (administrator)"
        )
    }

    override fun handleInteractions() {
        isActive = true
        do {
            displayMenu()
            when (getOption()) {
                AuthenticationMenuOption.RegisterVisitor -> registerVisitor()
                AuthenticationMenuOption.RegisterAdmin -> registerAdmin()
                AuthenticationMenuOption.LoginVisitor -> loginVisitor()
                AuthenticationMenuOption.LoginAdmin -> loginAdmin()
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
        DI.inputManager.showPrompt("To register a new account with admin rights provide the security token: ")
        val token = DI.inputManager.getString()
        if (!authenticationController.CheckSecurityToken(token)) {
            println("Invalid authentication token.")
            return
        }

        val (accountName, password, passwordMatch) = getAccountInfoWithConfirmation()

        if(!passwordMatch) {
            println("Passwords do not match. Aborting account registration...")
            return
        }

        println(authenticationController.registerAdminAccount(accountName, password))
    }

    private fun registerVisitor() {
        val (accountName, password) = getAccountInfo()

        val response = authenticationController.registerVisitorAccount(accountName, password)
        if(response.first.status == Status.Success && currentAccount != null)
            currentAccount = response.second
        println(response.first)
    }

    private fun loginVisitor() {
        val (accountName, password) = getAccountInfo()

        val response = authenticationController.LogIntoVisitorAccount(accountName, password)
        if(response.first.status == Status.Success && currentAccount != null)
            currentAccount = response.second
        println(response.first)
    }

    private fun loginAdmin() {
        val (accountName, password) = getAccountInfo()

        val response = authenticationController.LogIntoAdminAccount(accountName, password)
        if(response.first.status == Status.Success && currentAccount != null)
            currentAccount = response.second
        println(response.first)
    }

    private fun getAccountInfo() : Pair<String, String>{
        DI.inputManager.showPrompt("Enter the name of the account: ")
        val accountName = DI.inputManager.getString()

        DI.inputManager.showPrompt("Enter the password: ")
        val password = DI.inputManager.getString()

        return Pair(accountName, password)
    }

    private fun getAccountInfoWithConfirmation() : Triple<String, String, Boolean>{
        val (accountName, password) = getAccountInfo()
        DI.inputManager.showPrompt("Enter the password again: ")
        val passwordConfirmation = DI.inputManager.getString()

        return Triple(accountName, password, password == passwordConfirmation)
    }

    private fun getOption(): AuthenticationMenuOption? {
        return readlnOrNull()?.let { parseAction(it) }
    }
}
