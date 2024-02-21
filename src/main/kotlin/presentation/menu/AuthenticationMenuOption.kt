package presentation.menu

enum class AuthenticationMenuOption(private val value: Int) {
    RegisterVisitor(1),
    RegisterAdmin(2),
    LoginVisitor(3),
    LoginAdmin(4),
    AuthorizeWithCode(5),
    Exit(6),
}