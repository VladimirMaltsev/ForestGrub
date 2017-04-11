package BTreeLib

import java.util.*


class BTree<Key : Comparable<Key>, Data> : Iterable<BNode<Key, Data>> : Tree<Key, Data>, Iterable<RBNode<Key, Data>>{

    private var root: BNode<Key, Data> = BNode()

    companion object {
        public const val MIN_DIG: Int = 3
    }

    fun isEmpty() = root.keys.size == 0
    fun getRoot() = root

    fun search(key: Key): Data? {
        var node = searchNode(key = key) ?: return null

        var i = 0
        while (i < node.keys.size && key > node.keys[i]) {
            i++
        }
        if (i < node.keys.size && key == node.keys[i])
            return node.data[i]

        return null
    }

    private fun searchNode(node: BNode<Key, Data> = root,
                           key: Key, isDelete: Boolean = false): BNode<Key, Data>? {
        printTree()
        var i = 0
        while (i < node.keys.size && key > node.keys[i]) {
            i++
            print(node.keys[i])
            print(" ")
        }
        println(i)

        if (i < node.keys.size && key == node.keys[i])
            return node

        if (node.isLeaf())
            return null



        return searchNode(node.children[i], key, isDelete)
    }

    private fun splitNode(parent: BNode<Key, Data>, i_median: Int, splitChild: BNode<Key, Data>) {
        var bro_node = BNode<Key, Data>()
        for (ind in 0..MIN_DIG - 2) {
            bro_node.keys.add(0, splitChild.keys.removeAt(splitChild.keys.size - 1)) //очень тяжелая операция
            bro_node.data.add(0, splitChild.data.removeAt(splitChild.data.size - 1))
        }

        if (!splitChild.isLeaf()) {
            for (ind in 0..MIN_DIG - 1)
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
        while (i >= 0 && key < currNode.keys[i])
            i--
        i++

        if (currNode.isLeaf()) {
            currNode.keys.add(i, key)
            currNode.data.add(i, data)
        } else {

            if (currNode.children[i].isFull()) {
                splitNode(currNode, i, currNode.children[i])
                if (key > currNode.keys[i]) {
                    i++
                }
            }

            insert_fallout(currNode.children[i], key, data)
        }
    }

    fun remove (key: Key, currNode : BNode<Key, Data> = root) {
        //находим ключ который >= key
        var i = 0
        while (i < currNode.keys.size && key > currNode.keys[i]) {
            i++
        }

        //в случае если ключ найден
        if (i < currNode.keys.size && key == currNode.keys[i]) {

            //если лист то просто удаляем
            if (currNode.isLeaf()) {

                currNode.keys.removeAt(i)
                currNode.data.removeAt(i)
            } else {

                //иначе ищем приемника
                var node_victim = currNode

                //попытка отжать ключ у левого ребенка
                if (node_victim.children[i].keys.size >= MIN_DIG) {
                    node_victim = node_victim.children[i]
                    while (!node_victim!!.isLeaf()) {
                        node_victim = node_victim.children[node_victim.children.size - 1]
                    }

                    var new_key = node_victim.keys[node_victim.keys.size - 1]
                    var new_data = node_victim.data[node_victim.data.size - 1]

                    remove(new_key, currNode)

                    currNode.keys[i] = new_key
                    currNode.data[i] = new_data

                } else {
                    //пытаемся отжать ключ у правого потомка
                    if (node_victim.children[i + 1].keys.size >= MIN_DIG) {
                        node_victim = node_victim.children[i + 1]
                        while (!node_victim!!.isLeaf()) {
                            node_victim = node_victim.children[0]
                        }
                        var new_key = node_victim.keys[0]
                        var new_data = node_victim.data[0]

                        remove(new_key, currNode)

                        currNode.keys[i] = new_key
                        currNode.data[i] = new_data
                    } else {
                        //если оба ребенка оказались малышами, то мерджим их
                        currNode.children[i].keys.add(currNode.keys[i])
                        currNode.children[i].data.add(currNode.data[i])

                        while (!currNode.children[i + 1].keys.isEmpty()) {
                            currNode.children[i].keys.add(currNode.children[i + 1].keys.removeAt(0))
                            currNode.children[i].data.add(currNode.children[i + 1].data.removeAt(0))
                        }

                        while (!currNode.children[i + 1].children.isEmpty()) {
                            currNode.children[i].children.add(currNode.children[i + 1].children.removeAt(0))
                        }

                        currNode.keys.removeAt(i)
                        currNode.data.removeAt(i)
                        currNode.children.removeAt(i + 1)
                        remove(key, currNode.children[i])
                    }
                }
            }
        } else { //если же ключ не найден
            println("key not found")
            //если лист - все плохо его нет
            if (currNode.isLeaf())
                return

            //далее мы должны пойти в ребенка с индексом i
            //при этом мы должны быть уверены, что удалив там ключ, у нас не нарушится структура,
            //поэтому надо следить за тем, чтобы ключей в каждой вершине по пути следования было
            // >= чем минимальная степень дерева

            if (currNode.children[i].keys.size < BTree.MIN_DIG) {

                //пытаемся подрезать ключ у правого соседа
                //если мы сами не являемся ультраправыми
                if (i + 1 < currNode.children.size && currNode.children[i + 1].keys.size >= BTree.MIN_DIG) {
                    var key_parent = currNode.keys[i]
                    var data_parent = currNode.data[i]

                    currNode.keys[i] = currNode.children[i + 1].keys.removeAt(0)
                    currNode.data[i] = currNode.children[i + 1].data.removeAt(0)

                    currNode.children[i].keys.add(key_parent)
                    currNode.children[i].data.add(data_parent)

                    if (!currNode.children[i].isLeaf())
                        currNode.children[i].children.add(currNode.children[i + 1].children.removeAt(0))
                    remove(key, currNode.children[i])
                } else

                    //у правого не получилось, может у левого получиться
                    if (i - 1 >= 0 && currNode.children[i - 1].keys.size >= BTree.MIN_DIG) {
                        var key_parent = currNode.keys[i - 1]
                        var data_parent = currNode.data[i - 1]

                        currNode.keys[i - 1] = currNode.children[i - 1].keys.removeAt(currNode.children[i - 1].keys.size - 1)
                        currNode.data[i - 1] = currNode.children[i - 1].data.removeAt(currNode.children[i - 1].data.size - 1)

                        currNode.children[i].keys.add(0, key_parent)
                        currNode.children[i].data.add(0, data_parent)
                        if (!currNode.children[i - 1].isLeaf())
                            currNode.children[i].children.add(0, currNode.children[i - 1].children.removeAt(currNode.children[i - 1].children.size - 1))
                        remove(key, currNode.children[i])
                    } else {
                        //ни в право ни в левом взять ключ нельзя
                        //придется мерджить с одним из соседей

                        if (i + 1 < currNode.children.size) {
                            currNode.children[i].keys.add(currNode.keys.removeAt(i))
                            currNode.children[i].data.add(currNode.data.removeAt(i))

                            while (!currNode.children[i + 1].keys.isEmpty()) {
                                currNode.children[i].keys.add(currNode.children[i + 1].keys.removeAt(0))
                                currNode.children[i].data.add(currNode.children[i + 1].data.removeAt(0))
                            }

                            if (!currNode.children[i + 1].isLeaf())
                                while (!currNode.children[i + 1].children.isEmpty()) {
                                    currNode.children[i].children.add(currNode.children[i + 1].children.removeAt(0))
                                }
                            currNode.children.removeAt(i + 1)

                            remove(key, currNode.children[i])
                        } else {
                            //мерджим с левым
                            currNode.keys.add(currNode.keys.removeAt(i - 1))
                            currNode.children[i].data.add(currNode.data.removeAt(i - 1))

                            while (!currNode.children[i - 1].keys.isEmpty()) {
                                currNode.children[i].keys.add(0, currNode.children[i - 1].keys.removeAt(currNode.children[i - 1].keys.size - 1))
                                currNode.children[i].data.add(0, currNode.children[i - 1].data.removeAt(currNode.children[i - 1].data.size - 1))
                            }

                            if (!currNode.children[i - 1].isLeaf())
                                while (!currNode.children[i - 1].children.isEmpty()) {
                                    currNode.children[i].children.add(0, currNode.children[i - 1].children.removeAt(currNode.children[i - 1].children.size - 1))
                                }
                            currNode.children.removeAt(i - 1)
                            remove(key, currNode)
                        }
                    }

            } else
                remove(key, currNode.children[i])


        }
        if (root.isEmpty())
            if (!root.children.isEmpty())
                root = root.children.removeAt(0)

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

    override fun iterator(): Iterator<BNode<Key, Data>> {
        return (object : Iterator<BNode<Key, Data>>
        {
            var nodes : Queue<BNode<Key, Data>> = LinkedList()

            init {
                if (!root.isEmpty())
                    nodes.add(root)
            }
            override fun hasNext(): Boolean {
                return nodes.isEmpty()
            }

            override fun next(): BNode<Key, Data> {
                var curNode = nodes.poll()
                if (!curNode.isLeaf()) {
                    for (child in curNode.children) {
                        nodes.add(child)
                    }
                }
                return curNode
            }

        })
    }

}