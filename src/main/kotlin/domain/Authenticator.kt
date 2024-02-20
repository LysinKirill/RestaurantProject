package domain

interface Authenticator<T> {
    fun verify(objectToVerify: T) : Boolean
}