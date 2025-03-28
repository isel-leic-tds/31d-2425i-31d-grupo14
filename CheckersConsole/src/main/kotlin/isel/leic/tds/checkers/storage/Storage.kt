package isel.leic.tds.checkers.storage

interface Storage<Key,Data: Any?> {
    fun create(key: Key, data: Data)
    fun read(key: Key): Data?
    fun update(key: Key, data: Data)
    fun delete(key: Key)
}