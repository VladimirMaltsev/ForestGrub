package TreeLib

import ForestGun.TreeLib.COLOR
import com.sun.org.apache.xpath.internal.operations.Bool

class RBNode<Key, Data>(var key: Key, var data: Data){

    var color: COLOR = COLOR.RED

    var parent: RBNode<Key, Data>? = null
    var leftChild: RBNode<Key, Data>? = null
    var rightChild: RBNode<Key, Data>? = null

    override operator fun equals(other: Any?): Boolean {
        if (other is RBNode<*, *>?)
        {
            if (other == null)
                return false

            if (this.key == other.key &&
                    this.data == other.data &&
                    this.color == other.color /*&&
                    this.parent == other.parent &&
                    this.rightChild == other.rightChild&&
                    this.leftChild == other.leftChild*/)
                return true

            return false
        }
        return false
    }
}