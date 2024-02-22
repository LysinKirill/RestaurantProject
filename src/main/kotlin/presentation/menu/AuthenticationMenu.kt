package presentation.menu

import domain.AuthenticationController
import domain.entity.AccountEntity
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
            //println("Current account: ${if (currentAccount == null) "not logged in" else currentAccount?.name}")
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
        val response = authenticationController.registerAdminAccount(queryingAccount = currentAccount)
        println(response.first)
        if (response.first.status == Status.Success && response.second != null) {
            currentAccount = response.second
            isActive = false
            println("Logged in as administrator: ${response.second?.name}")
        }
    }

    private fun registerVisitor() {
        val response = authenticationController.registerVisitorAccount()
        println(response.first)
        if (response.first.status == Status.Success && response.second != null) {
            currentAccount = response.second
            isActive = false
            println("Logged in as visitor: ${response.second?.name}")
        }
    }

    private fun loginVisitor() {
        val response = authenticationController.logIntoVisitorAccount()
        println(response.first)
        if (response.first.status == Status.Success && response.second != null) {
            currentAccount = response.second
            isActive = false
            //println("Logged in as visitor: ${response.second?.name}")
        }
    }

    private fun loginAdmin() {
        val response = authenticationController.logIntoAdminAccount()
        println(response.first)
        if (response.first.status == Status.Success && response.second != null) {
            currentAccount = response.second
            isActive = false
            //println("Logged in as administrator: ${response.second?.name}")
        }
    }

    private fun authorizeWithCode() {
        val response = authenticationController.logInAsSuperuser()
        println(response.first)
        if (response.first.status == Status.Success && response.second != null) {
            currentAccount = response.second
            isActive = false
            //println("Logged in as administrator: ${response.second?.name}")
        }
        //println("The provided security code does not match the security code of the superuser. Could not log into an account.")
    }



    private fun getOption(): AuthenticationMenuOption? {
        return readlnOrNull()?.let { parseAction(it) }
    }
}
