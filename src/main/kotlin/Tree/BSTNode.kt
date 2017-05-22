package TreeLib

class BSTNode<Key, Data>  (var key: Key, var data: Data)
{
     internal var parent: BSTNode<Key, Data>? = null
     internal var leftChild: BSTNode<Key, Data>? = null
     internal var rightChild: BSTNode<Key, Data>? = null

//     override operator fun equals(other: Any?): Boolean {
//          if (other is BSTNode<*, *>?)
//          {
//               if (other == null)
//                    return false
//
//               if (this.key == other.key &&
//                       this.data == other.data &&
//                    this.parent == other.parent &&
//                    this.rightChild == other.rightChild &&
//                    this.leftChild == other.leftChild)
//                    return true
//
//               return false
//          }
//          return false
//     }
}