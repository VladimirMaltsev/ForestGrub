package ForestGun.TreeUtils

import ForestGun.TreeLib.COLOR
import TreeLib.RBNode
import TreeLib.RBTree

fun <Key:Comparable<Key>, Data> RBTree<Key, Data>
        .getMin(defaultNode: RBNode<Key, Data>? = root) : RBNode<Key, Data>? {

    var minNode: RBNode<Key, Data>? = defaultNode ?: return null

    while (minNode!!.leftChild != null)
        minNode = minNode.leftChild
    return minNode
}

fun <Key:Comparable<Key>, Data> RBTree<Key, Data>
        .getMax(defaultNode: RBNode<Key, Data>? = root) : RBNode<Key, Data>? {

    var maxNode: RBNode<Key, Data>? = defaultNode ?: return null

    while (maxNode!!.rightChild != null)
        maxNode = maxNode.rightChild
    return maxNode
}

fun<Key:Comparable<Key>, Data> RBTree<Key, Data>
    .getNodeHeightByKey(key: Key): Int {

    var height = 0
    var currNode = root
    while (currNode != null && currNode.key != key) {

        currNode = if (key > currNode.key) currNode.rightChild else currNode.leftChild
        height++
    }

    return height
}


fun<Key:Comparable<Key>, Data> RBTree<Key, Data>
        .getBlackHeightOf(node : RBNode<Key, Data>?): Int {


    if (node == null)
        return 0

    //print(node.key)
    //print("  ")

    var height = 0
    var currNode: RBNode<Key, Data>? = root ?: return 0

    while (currNode!! != node) {

        if (currNode!!.color == COLOR.BLACK)
            height++

        currNode = if (node.key > currNode.key) currNode.rightChild else currNode.leftChild

        if (currNode == null)
            break
    }

    if (node.color == COLOR.BLACK)
        height ++

    //println(height)
    return height
}