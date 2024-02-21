package presentation.menu

interface ResponsiveMenu<ResponseType> : Menu {
    fun getResponse() : ResponseType?
}