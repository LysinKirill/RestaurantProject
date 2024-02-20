package di

import domain.AuthenticationController
import domain.Authenticator
import domain.ConsoleInputManager
import domain.InputManager
import presentation.MenuFactory

object DI {
    val authenticator: Authenticator<String>
        get() = // some authenticator

    val inputManager: InputManager
        get() = ConsoleInputManager()

    val authenticationController: AuthenticationController
        get() = AuthenticationController()
}