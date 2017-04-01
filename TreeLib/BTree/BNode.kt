package BTreeLib


class BNode<Key : Comparable<Key>, Data> {
    var keys = ArrayList<Key> ()
    var data = ArrayList<Data> ()
    var children = ArrayList<BNode<Key, Data>> ()

    fun isLeaf () = children.size == 0

    fun isFull () = keys.size == 2 * BTree.MIN_DIG - 1
}