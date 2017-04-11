package ForestGun.TreeLib

import ForestGun.TreeUtils.*
import TreeLib.RBNode
import TreeLib.*
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int

class TreePrinter<Key :Comparable<Key>, Data> (var tree : RBTree<Key, Data>) {

    public val POSTORDER = 2

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
            POSTORDER ->
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
        }
    }
}
