package presentation.menu.interfaces

interface ResponsiveMenu<ResponseType> : Menu {
    fun getResponse() : ResponseType?
}