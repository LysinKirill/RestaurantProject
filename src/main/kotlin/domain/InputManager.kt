package domain

interface InputManager {
    fun showPrompt(prompt: String)
    fun getString() : String
    fun getInt() : Int
    fun getDouble() : Double
}