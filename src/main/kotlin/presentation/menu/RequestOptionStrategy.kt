package presentation.menu

interface RequestOptionStrategy<T: Enum<T>> {
    fun requestOption() : T?
}