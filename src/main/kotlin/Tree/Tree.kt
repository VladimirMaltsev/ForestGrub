package TreeLib

interface Tree <Key : Comparable<Key>, Data> {

        fun insert(key: Key, data: Data)

        fun search(key: Key): Data?

        fun remove(key: Key): Boolean

}