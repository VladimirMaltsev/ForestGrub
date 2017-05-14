package ForestGun.TreeLib

import ForestGun.TreeUtils.*
import TreeLib.RBNode
import TreeLib.*

class TreePrinter<Key :Comparable<Key>, Data> (var tree : RBTree<Key, Data>) {

    public val POST_ORDER = 2
    public val INFO_ABOUT_EACH_NODE = 1

    var getNextPostOrder: (RBNode<Key, Data>?) -> RBNode<Key, Data>? = {
        var node = it;

        if (node!!.leftChild != null)
            node = tree.getMax(node.leftChild)
        else {
            while (node!!.parent != null && node == node.parent!!.leftChild)
                node = node.parent
            node = node.parent
        }
        node
    }

    public fun printTree(case: Int) {
        when (case)
        {
            POST_ORDER ->
            {
                val iterator = tree.iterator(getNextPostOrder)
                while (iterator.hasNext())
                {
                    var node = iterator.next()

                    for (i in 1..tree.getNodeHeightByKey(node.key))
                        print("  ")

                    var spec_symbol = "";

                    if (node.parent != null)
                        if (node == node.parent!!.leftChild)
                            spec_symbol = "\\"
                        else
                            spec_symbol = "/"

                    println("$spec_symbol${27.toChar()}${if (node.color == COLOR.RED) "[31m" else "[1m"}${node.data}${27.toChar()}[0m")
                }
            }
            INFO_ABOUT_EACH_NODE -> {
                val iterator = tree.iterator(getNextPostOrder)
                while (iterator.hasNext())
                {
                    var node = iterator.next()
                    println("node key = ${node.key}")
                    println("node parent = ${node.parent?.key}")
                    println("node leftChild = ${node.leftChild?.key}")
                    println("node rightChild = ${node.rightChild?.key}")
                    println()
                }

            }
        }
    }
}
