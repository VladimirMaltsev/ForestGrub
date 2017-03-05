package ForestGun.TreeLib

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

    public fun printTree (case : Int){
        when (case){
            POSTORDER -> printTree(getNextPostOrder)
        }
    }

    public fun printTree(orderNext : (RBNode<Key, Data>?) -> RBNode<Key, Data>?){
        val iterator = tree.iterator(orderNext)
        while (iterator.hasNext()){
            print("${iterator.next().data} ")
        }
    }
}