package domain

class ConsoleInputManager : InputManager {
    override fun showPrompt(prompt: String) {
        println(prompt)
    }

    override fun getString() : String {
        while(true) {
            try {
                val input = readlnOrNull()
                if(input == null) {
                    println("Could not read a string from the console. Try again...")
                    continue
                }
                return input
            } catch(_:Exception) {
                println("Could not read a string from the console. Try again...")
            }
        }
    }

    override fun getInt(): Int {
        while(true) {
            try {
                val input = readlnOrNull()
                if(input == null) {
                    println("Could not read an integer from the console. Try again...")
                    continue
                }
                return input.toInt()
            } catch(_:Exception) {
                println("Could not read an integer from the console. Try again...")
            }
        }
    }

    override fun getDouble(): Double {
        while(true) {
            try {
                val input = readlnOrNull()
                if(input == null) {
                    println("Could not read a float value from the console. Try again...")
                    continue
                }
                return input.toDouble()
            } catch(_:Exception) {
                println("Could not read a float value from the console. Try again...")
            }
        }
    }
}