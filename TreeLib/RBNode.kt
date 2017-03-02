package TreeLib

import com.sun.org.apache.xpath.internal.operations.Bool

class RBNode<Key, Data>(var key: Key, var data: Data){

    var isRed: Boolean = true

    var parent: RBNode<Key, Data>? = null
    var leftChild: RBNode<Key, Data>? = null
    var rightChild: RBNode<Key, Data>? = null

}