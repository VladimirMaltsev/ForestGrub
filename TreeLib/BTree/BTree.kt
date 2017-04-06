package BTreeLib

import java.util.*


class BTree<Key : Comparable<Key>, Data> {

    private var root: BNode<Key, Data> = BNode()

    companion object {
        public const val MIN_DIG: Int = 3
    }

    fun isEmpty() = root.keys.size == 0

    fun search(key: Key): Data? {
        var (parent, node) = searchNode(key = key)
        if (node == null)
            return null

        var i = 0
        while (i < node.keys.size && key > node.keys[i]) {
            i++
        }
        if (i < node.keys.size && key == node.keys[i])
            return node.data[i]

        return null
    }

    private fun searchNode(parent: BNode<Key, Data> = root, node: BNode<Key, Data> = root,
                           key: Key, isDelete: Boolean = false): Pair<BNode<Key, Data>?, BNode<Key, Data>?> {
        var i = 0
        while (i < node.keys.size && key > node.keys[i])
            i++

        if (i < node.keys.size && key == node.keys[i])
            return Pair(parent, node)

        if (node.isLeaf())
            return Pair(null, null)

        if (isDelete && node.children[i].keys.size < MIN_DIG) {
            if (i + 1 < node.children.size && node.children[i + 1].keys.size >= MIN_DIG) run {
                var key_parent = node.keys[i]
                var data_parent = node.data[i]

                node.keys.add(i, node.children[i + 1].keys.removeAt(0))
                node.data.add(i, node.children[i + 1].data.removeAt(0))

                node.children[i].keys.add(key_parent)
                node.children[i].data.add(data_parent)

                node.children[i].children.add(node.children[i + 1].children.removeAt(0))
            } else if (i - 1 < node.children.size && node.children[i - 1].keys.size >= MIN_DIG) run {
                var key_parent = node.keys[i - 1]
                var data_parent = node.data[i - 1]

                node.keys.add(i - 1, node.children[i - 1].keys.removeAt(node.children[i - 1].keys.size - 1))
                node.data.add(i - 1, node.children[i - 1].data.removeAt(node.children[i - 1].data.size - 1))

                node.children[i].keys.add(0, key_parent)
                node.children[i].data.add(0, data_parent)

                node.children[i].children.add(0, node.children[i - 1].children.removeAt(node.children[i - 1].children.size - 1))
            } else {
                if (i + 1 < node.children.size) {
                    node.children[i].keys.add(node.keys[i])
                    node.children[i].data.add(node.data[i])

                    while (!node.children[i + 1].keys.isEmpty()) {
                        node.children[i].keys.add(node.children[i + 1].keys.removeAt(0))
                        node.children[i].data.add(node.children[i + 1].data.removeAt(0))
                    }

                    while (!node.children[i + 1].children.isEmpty()) {
                        node.children[i].children.add(node.children[i + 1].children.removeAt(0))
                    }

                    node.keys.removeAt(i)
                    node.data.removeAt(i)
                    node.children.removeAt(i + 1)
                } else {
                    node.children[i].keys.add(0, node.keys[i - 1])
                    node.children[i].data.add(0, node.data[i - 1])

                    while (!node.children[i - 1].keys.isEmpty()) {
                        node.children[i].keys.add(0, node.children[i - 1].keys.removeAt(node.children[i - 1].keys.size - 1))
                        node.children[i].data.add(0, node.children[i - 1].data.removeAt(node.children[i - 1].data.size - 1))
                    }

                    while (!node.children[i - 1].children.isEmpty()) {
                        node.children[i].children.add(0, node.children[i - 1].children.removeAt(node.children[i - 1].children.size - 1))
                    }

                    node.keys.removeAt(i - 1)
                    node.data.removeAt(i - 1)
                    node.children.removeAt(i - 1)
                }
            }
        }

        return searchNode(node, node.children[i], key)
    }

    private fun splitNode(parent: BNode<Key, Data>, i_median: Int, splitChild: BNode<Key, Data>) {
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

    private fun insert_fallout(currNode: BNode<Key, Data>, key: Key, data: Data) {
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

    fun delete(key: Key) {
        var (parent, maggie) = searchNode(key = key, isDelete = true) //maggie is a girl who was wanted to be die
        if (maggie == null)
            return

        var ind = 0
        while (ind < maggie.keys.size && key > maggie.keys[ind]) {
            ind++
        }

        while (!maggie!!.isLeaf()) {
            var node_victim = maggie
            if (node_victim!!.children[ind].keys.size >= MIN_DIG) {
                node_victim = node_victim.children[ind]
                while (!node_victim!!.isLeaf()) {
                    node_victim = node_victim.children[node_victim.children.size - 1]
                }
                maggie.keys[ind] = node_victim.keys[node_victim.keys.size - 1]
                maggie.data[ind] = node_victim.data[node_victim.data.size - 1]

                ind = node_victim.keys.size - 1
                maggie = node_victim
            } else {
                if (node_victim.children[ind + 1].keys.size >= MIN_DIG) {
                    node_victim = node_victim!!.children[ind + 1]
                    while (!node_victim!!.isLeaf()) {
                        node_victim = node_victim.children[0]
                    }
                    maggie.keys[ind] = node_victim.keys[0]
                    maggie.data[ind] = node_victim.data[0]

                    ind = 0
                    maggie = node_victim
                } else {
                    maggie.children[ind].keys.add(maggie.keys[ind])
                    maggie.children[ind].data.add(maggie.data[ind])

                    while (!maggie.children[ind + 1].keys.isEmpty()) {
                        maggie.children[ind].keys.add(maggie.children[ind + 1].keys.removeAt(0))
                        maggie.children[ind].data.add(maggie.children[ind + 1].data.removeAt(0))
                    }

                    while (!maggie.children[ind + 1].children.isEmpty()) {
                        maggie.children[ind].children.add(maggie.children[ind + 1].children.removeAt(0))
                    }

                    maggie.keys.removeAt(ind)
                    maggie.data.removeAt(ind)
                    maggie.children.removeAt(ind + 1)
                    maggie = maggie.children[ind]
                    ind = maggie.keys.indexOf(key)
                }
            }
        }

        maggie.data.removeAt(ind)
        maggie.keys.removeAt(ind)
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