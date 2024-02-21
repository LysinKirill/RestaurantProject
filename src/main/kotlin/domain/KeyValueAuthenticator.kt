package domain

interface KeyValueAuthenticator<KeyType, ValueType> {
    fun verify(key: KeyType, value: ValueType) : Boolean
}