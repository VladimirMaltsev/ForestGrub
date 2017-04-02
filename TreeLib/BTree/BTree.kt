package BTreeLib

import java.util.*


class BTree<Key : Comparable<Key>, Data> {

    private var root: BNode<Key, Data> = BNode()

    companion object {
        public const val MIN_DIG: Int = 3
    }

    fun isEmpty() = root.keys.size == 0

    fun search(node: BNode<Key, Data> = root, key: Key): Data? {

        var i = 0
        while (i < node.keys.size && key > node.keys[i])
            i++

        if (i < node.keys.size && key == node.keys[i])
            return node.data[i]

        if (node.isLeaf())
            return null

        return search(node.children[i], key)
    }

    fun splitNode(parent: BNode<Key, Data>, i_median: Int, splitChild: BNode<Key, Data>) {
        var bro_node = BNode<Key, Data>()
        for (ind in 0..MIN_DIG - 2) {
            bro_node.keys.add(0, splitChild.keys.removeAt(splitChild.keys.size - 1)) //очень тяжелая операция
            bro_node.data.add(0, splitChild.data.removeAt(splitChild.data.size - 1))
        }

        if (!splitChild.isLeaf()) {
            for (ind in 0..MIN_DIG)
                bro_node.children.add(0, splitChild.children.removeAt(splitChild.children.size - 1))
        }

        parent.children.add(i_median + 1, bro_node)
        parent.keys.add(i_median, splitChild.keys.removeAt(MIN_DIG - 1))
        parent.data.add(i_median, splitChild.data.removeAt(MIN_DIG - 1))
    }

    fun insert(key: Key, data: Data) {
        if (root.isFull()) {
            var newRoot = BNode<Key, Data>()
            newRoot.children.add(root)
            splitNode(newRoot, 0, root)
            root = newRoot
        }

        insert_fallout(root, key, data)
    }

    fun insert_fallout(currNode: BNode<Key, Data>, key: Key, data: Data) {
        var i = currNode.keys.size - 1
        if (currNode.isLeaf()) {
            while (i >= 0 && key < currNode.keys.get(i))
                i--
            i++
            currNode.keys.add(i, key)
            currNode.data.add(i, data)
        } else {
            while (i >= 0 && key < currNode.keys.get(i))
                i--
            i++
            if (currNode.children.get(i).isFull()) {
                splitNode(currNode, i, currNode.children.get(i))
                if (key > currNode.keys.get(i)) {
                    i++
                }
            }

            insert_fallout(currNode.children.get(i), key, data)
        }
    }

    fun printTree() {
        var list: Queue<BNode<Key, Data>> = LinkedList()
        var listChar: Queue<Char> = LinkedList()

        list.add(root)
        listChar.add('\n')

        var specSymbol = '\n'

        while (!list.isEmpty()) {
            var curNode = list.poll()

            for (key in curNode.keys)
                print(" $key ")

            if (!curNode.isLeaf()) {
                for (child in curNode.children) {
                    list.add(child)
                    listChar.add('|')
                }
            }
            if (listChar.peek() == '\n') {
                listChar.add(specSymbol)
                print(listChar.poll())
            }
            print(listChar.poll())
        }

    }


}