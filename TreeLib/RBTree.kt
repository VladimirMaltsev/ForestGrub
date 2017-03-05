package TreeLib

import java.util.*

class RBTree<Key : Comparable<Key>, Data>(private var root: RBNode<Key, Data>? = null) : Tree<Key, Data>, Iterable<RBNode<Key, Data>>
{

    var getNextNode: (RBNode<Key, Data>?) -> RBNode<Key, Data>? = {
        var node = it;

        if (node!!.leftChild != null)
            node = getMax(node.leftChild)
        else {
            while (node != root && node == node!!.parent!!.leftChild)
                node = node.parent
            node = node!!.parent
        }
        node
    }

    override fun insert(key: Key, data: Data) {
        var newNode: RBNode<Key, Data> = RBNode(key, data)

        if (root == null) {
            root = newNode
            return balanceTreeAfterInsert(root!!)
        }
        var currNode: RBNode<Key, Data>? = root

        while (currNode != null) {
            if (currNode.key > newNode.key) {
                if (currNode.leftChild == null) {
                    currNode.leftChild = newNode
                    newNode.parent = currNode
                    return balanceTreeAfterInsert(newNode)
                }
                currNode = currNode.leftChild;
            } else {
                if (currNode.rightChild == null) {
                    currNode.rightChild = newNode
                    newNode.parent = currNode
                    return balanceTreeAfterInsert(newNode)
                }
                currNode = currNode.rightChild
            }
        }
        return balanceTreeAfterInsert(newNode)
    }

    override fun search(key: Key): Data? {
        var currNode: RBNode<Key, Data>? = root

    while (currNode != null) {
        if (currNode.key == key) return currNode.data

        currNode = if (currNode.key > key) currNode.leftChild else currNode.rightChild
    }

    return null
}

    override fun remove(key: Key): Boolean {
        var victimNode = root

        //поиск узла-жертвы с заданным ключом
        while (victimNode != null && victimNode.key != key)
            victimNode = if (victimNode.key > key) victimNode.leftChild else victimNode.rightChild

        //узел не найден
        if (victimNode == null) return false;

        //определение узла, который будет удален
        var deletedNode: RBNode<Key, Data>? = victimNode

        if ((deletedNode!!.leftChild != null) && (deletedNode.rightChild != null)) {
            deletedNode = victimNode.rightChild
            while (deletedNode!!.leftChild != null)
                deletedNode = deletedNode.leftChild
        }

        //узел, который встанет на место удаленного узла
        val succNode = deletedNode.leftChild ?: deletedNode.rightChild

        //удаляем deletedNode
        succNode?.parent = deletedNode.parent

        if (deletedNode.parent != null)
            if (deletedNode.parent!!.leftChild == deletedNode)
                deletedNode.parent!!.leftChild = succNode
            else
                deletedNode.parent!!.rightChild = succNode
        else
            root = succNode

        if (deletedNode != victimNode) {
            victimNode.key = deletedNode.key
            victimNode.data = deletedNode.data
        }

        if (!deletedNode.isRed)
            if (succNode != null)
                balanceTreeAfterRemove(succNode)
            else if (root != null)
                balanceTreeAfterRemove(deletedNode, true)
        return true
    }

    fun printPostOrder(curNode: RBNode<Key, Data>? = root, divider: String = "") {
        if (curNode == null)
            return
        var spec_symbol = "";
        if (curNode.parent != null)
            if (curNode == curNode.parent!!.leftChild)
                spec_symbol = "\\"
            else
                spec_symbol = "/"

        printPostOrder(curNode.rightChild, divider + "   ")
        var color: String = "b"
        if (curNode.isRed)
            color = "r"
        println("$divider$spec_symbol${curNode.data}$color")
        printPostOrder(curNode.leftChild, divider + "   ")
    }

    private fun rotateLeft(centerNode: RBNode<Key, Data>) {
        val heirNode = centerNode.rightChild
        centerNode.rightChild = heirNode!!.leftChild

        heirNode.leftChild?.parent = centerNode
        heirNode.leftChild = centerNode
        heirNode.parent = centerNode.parent

        if (centerNode.parent == null)
            root = heirNode;
        centerNode.parent = heirNode
        if (centerNode == heirNode.parent?.leftChild) {
            heirNode.parent?.leftChild = heirNode;
        } else heirNode.parent?.rightChild = heirNode;
    }

    private fun rotateRight(centerNode: RBNode<Key, Data>) {
        val heirNode = centerNode.leftChild
        centerNode.leftChild = heirNode!!.rightChild

        heirNode.rightChild?.parent = centerNode
        heirNode.rightChild = centerNode
        heirNode.parent = centerNode.parent

        if (centerNode.parent == null)
            root = heirNode;
        centerNode.parent = heirNode
        if (centerNode == heirNode.parent?.leftChild) {
            heirNode.parent?.leftChild = heirNode;
        } else heirNode.parent?.rightChild = heirNode;
    }

    private fun balanceTreeAfterInsert(node: RBNode<Key, Data>) {
        var currNode = node;
        while (currNode != root && currNode.parent!!.isRed)
        {
            if (currNode.parent == currNode.parent!!.parent!!.leftChild)
            { //случай, когда папа левый ребенок дедушки
                val uncleNode = currNode.parent!!.parent!!.rightChild;
                if (uncleNode != null && uncleNode.isRed)
                {// + дядя красный
                    uncleNode.parent!!.isRed = true
                    uncleNode.isRed = false
                    currNode.parent!!.isRed = false
                    currNode = currNode.parent!!.parent ?: break
                } else
                {                                                    // + дядя черный
                    if (currNode == currNode.parent!!.rightChild)
                    {          // + новый узел - правый потомок => поворот
                        currNode = currNode.parent!!
                        rotateLeft(currNode)
                    }

                    currNode.parent!!.isRed = false
                    currNode.parent!!.parent?.isRed = true
                    rotateRight(currNode.parent!!.parent!!)
                }
            } else
            {  //родитель красный
                val uncleNode = currNode.parent!!.parent!!.leftChild;
                if (uncleNode != null && uncleNode.isRed)
                {// + дядя красный
                    uncleNode.parent!!.isRed = true
                    uncleNode.isRed = false
                    currNode.parent!!.isRed = false
                    currNode = currNode.parent!!.parent ?: break
                } else
                {// + дядя черный
                    if (currNode == currNode.parent!!.leftChild)
                    {// + новый узел - левый потомок => поворот
                        currNode = currNode.parent!!
                        rotateRight(currNode)
                    }

                    currNode.parent!!.isRed = false
                    currNode.parent!!.parent?.isRed = true
                    rotateLeft(currNode.parent!!.parent!!)
                }
            }
        }
        root?.isRed = false
    }

    private fun balanceTreeAfterRemove(currNode: RBNode<Key, Data>?, isNullProf: Boolean = false) {
        var isNull = isNullProf
        var node = currNode
        while (isNull || node != root && !node!!.isRed) {
            //println("isNull = $isNull , node.data = ${node!!.data} , node.parent = ${node.parent}")
            if (node!!.parent!!.leftChild == node || node.parent!!.leftChild == null) {
                var broNode = node.parent!!.rightChild

                //если бро-узел красный, то у него есть два черных потомка => сводим к случаю когда бро-узел черный
                if (broNode!!.isRed) {
                    broNode.isRed = false
                    node.parent!!.isRed = true
                    rotateLeft(node.parent!!)
                    broNode = node.parent!!.rightChild
                }
                //во всех оставшихся случаех бро-узел черный

                //если оба племянника черные, то меняем цвет бро-узла на красный и скидываем проблему на родителя
                if (!(broNode!!.leftChild != null && broNode.rightChild != null &&
                        broNode.rightChild!!.isRed && broNode.leftChild!!.isRed)) {
                    broNode.isRed = true
                    node = node.parent
                } else {
                    //если левый пельмяш красный, то поворот вправо вокруг бро-узла
                    if (broNode.leftChild != null && broNode.leftChild!!.isRed) {
                        broNode.leftChild!!.isRed = false
                        broNode.isRed = true
                        rotateRight(broNode)
                    }
                    //последний случай, если правый пельмяшь красный
                    broNode.isRed = node.parent!!.isRed
                    node.parent!!.isRed = false
                    broNode.rightChild?.isRed = false
                    rotateLeft(node.parent!!)
                    node = root
                }
            } else  //все тоже самое, только зеркально отраженное leftChild = rightChild
            {       //                                            rotateLeft = rotateRight и наоборот
                var broNode = node.parent!!.leftChild

                if (broNode!!.isRed) {
                    broNode.isRed = false
                    node.parent!!.isRed = true
                    rotateRight(node.parent!!)
                    broNode = node.parent!!.leftChild
                }

                if (!(broNode!!.leftChild != null && broNode.rightChild != null &&
                        broNode.rightChild!!.isRed && broNode.leftChild!!.isRed)) {
                    broNode.isRed = true
                    node = node.parent
                } else {

                    if (broNode.rightChild != null && broNode.rightChild!!.isRed) {
                        broNode.rightChild!!.isRed = false
                        broNode.isRed = true
                        rotateLeft(broNode)
                    }

                    broNode.isRed = node.parent!!.isRed
                    node.parent!!.isRed = false
                    broNode.leftChild?.isRed = false
                    rotateRight(node.parent!!)
                    node = root
                }
            }
            isNull = false
        }
        node?.isRed = false
    }



    public fun iterator(getNextFun : (RBNode<Key, Data>?) -> RBNode<Key, Data>?) : Iterator<RBNode<Key, Data>>
    {
        getNextNode = getNextFun;
        return iterator()
    }

    public override fun iterator(): Iterator<RBNode<Key, Data>>
    {
        return (object : Iterator<RBNode<Key, Data>>
        {
            var next = getMax()

            override fun hasNext(): Boolean
            {
                return next != null
            }

            override fun next(): RBNode<Key, Data>
            {
                var retNode = next
                next = getNextNode(next)

                return retNode!!
            }
        })
    }

    public fun getMax(defaultNode: RBNode<Key, Data>? = root) : RBNode<Key, Data>?
    {
        var maxNode = defaultNode;

        if (maxNode == null)
            return null

        while (maxNode!!.rightChild != null)
            maxNode = maxNode.rightChild
        return maxNode
    }

    public fun getMin(defaultNode: RBNode<Key, Data>? = root) : RBNode<Key, Data>?
    {
        var minNode = root;

        if (minNode == null)
            return null

        while (minNode!!.leftChild != null)
            minNode = minNode.leftChild
        return minNode
    }
}
