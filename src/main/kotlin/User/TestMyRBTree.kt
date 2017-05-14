package User

import ForestGun.TreeLib.TreePrinter
import TreeLib.BSTree
import TreeLib.RBTree

fun main(args: Array<String>) {
    var bsTree: BSTree<Int, Int> = BSTree();
    var rbTree : RBTree<Int, Int> = RBTree();
    val treePrinter = TreePrinter<Int, Int>(rbTree)
    var data : Array<Int> = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
    for (i in data){
        rbTree.insert(i, i)
    }

    println()
    for (i in data){
        rbTree.remove(i)
//        treePrinter.printTree(2)
//        treePrinter.printTree(1)
    }
    //rbTree.remove(3)
}