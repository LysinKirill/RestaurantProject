package presentation.menu.interfaces

interface RequestOptionStrategy<T: Enum<T>> {
    fun requestOption() : T?
}