package presentation.menu

class VisitorMenu : Menu {
    override fun displayMenu() {
        println("Sample visitor menu")
    }

    override fun handleInteractions() {
        println("Handling interactions (visitor)...")
    }

}
