package TreeLib

import com.sun.org.apache.xpath.internal.operations.Bool

class RBNode<Key, Data>(var key: Key, var data: Data){

    var isRed: Boolean = true

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
                    this.isRed == other.isRed /*&&
                    this.parent == other.parent &&
                    this.rightChild == other.rightChild&&
                    this.leftChild == other.leftChild*/)
                return true

            return false
        }
        return false
    }
}