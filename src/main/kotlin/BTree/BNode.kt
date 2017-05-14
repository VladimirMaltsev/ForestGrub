package BTreeLib


class BNode<Key : Comparable<Key>, Data> {
    var keys = ArrayList<Key> ()
    var data = ArrayList<Data> ()
    var children = ArrayList<BNode<Key, Data>> ()

    fun isLeaf () = children.size == 0

    fun isEmpty () = keys.size == 0
}