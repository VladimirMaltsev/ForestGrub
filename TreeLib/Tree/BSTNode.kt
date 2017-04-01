package TreeLib

class BSTNode<Key, Data>  (var key: Key, var data: Data)
{
     internal var parent: BSTNode<Key, Data>? = null
     internal var leftChild: BSTNode<Key, Data>? = null
     internal var rightChild: BSTNode<Key, Data>? = null

}