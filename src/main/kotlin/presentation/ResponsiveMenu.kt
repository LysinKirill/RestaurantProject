package presentation

interface ResponsiveMenu<ResponseType> : Menu {
    fun getResponse() : ResponseType?
}