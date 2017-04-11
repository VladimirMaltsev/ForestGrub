package TreeLib

import ForestGun.TreeLib.COLOR
import ForestGun.TreeUtils.*

class RBTree<Key : Comparable<Key>, Data> : Tree<Key, Data>, Iterable<RBNode<Key, Data>> {
    var root: RBNode<Key, Data>? = null
        private set

    var getNextNode: (RBNode<Key, Data>?) -> RBNode<Key, Data>? = {
        var node = it

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
                currNode = currNode.leftChild

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

            if (currNode.key == key)
                return currNode.data

            currNode = if (currNode.key > key) currNode.leftChild else currNode.rightChild
        }

        return null
    }

    override fun remove(key: Key): Boolean {
        var victimNode = root

        //поиск узла-жертвы с заданным ключом
        //victim - ЖЕРТВА
        while (victimNode != null && victimNode.key != key)
            victimNode = if (victimNode.key > key) victimNode.leftChild else victimNode.rightChild

        //узел не найден
        if (victimNode == null)
            return false

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

        if (deletedNode.color == COLOR.BLACK)

            if (succNode != null)
                balanceTreeAfterRemove(succNode)
            else if (root != null)
                balanceTreeAfterRemove(deletedNode, true)

        return true
    }

    private fun rotateLeft(centerNode: RBNode<Key, Data>) {

        val heirNode = centerNode.rightChild
        centerNode.rightChild = heirNode!!.leftChild

        heirNode.leftChild?.parent = centerNode
        heirNode.leftChild = centerNode
        heirNode.parent = centerNode.parent

        if (centerNode.parent == null)
            root = heirNode

        centerNode.parent = heirNode

        if (centerNode == heirNode.parent?.leftChild) {
            heirNode.parent?.leftChild = heirNode
        } else
            heirNode.parent?.rightChild = heirNode
    }

    private fun rotateRight(centerNode: RBNode<Key, Data>) {

        val heirNode = centerNode.leftChild
        centerNode.leftChild = heirNode!!.rightChild

        heirNode.rightChild?.parent = centerNode
        heirNode.rightChild = centerNode
        heirNode.parent = centerNode.parent

        if (centerNode.parent == null)
            root = heirNode

        centerNode.parent = heirNode

        if (centerNode == heirNode.parent?.leftChild) {
            heirNode.parent?.leftChild = heirNode
        } else
            heirNode.parent?.rightChild = heirNode
    }

    private fun balanceTreeAfterInsert(node: RBNode<Key, Data>) {

        var currNode = node
        while (currNode != root && currNode.parent!!.color == COLOR.RED) {

            if (currNode.parent == currNode.parent!!.parent!!.leftChild) { //случай, когда папа левый ребенок дедушки

                val uncleNode = currNode.parent!!.parent!!.rightChild

                if (uncleNode != null && uncleNode.color == COLOR.RED) {// + дядя красный

                    uncleNode.parent!!.color = COLOR.RED
                    uncleNode.color = COLOR.BLACK
                    currNode.parent!!.color = COLOR.BLACK
                    currNode = currNode.parent!!.parent ?: break
                } else {                                                    // + дядя черный

                    if (currNode == currNode.parent!!.rightChild) {          // + новый узел - правый потомок => поворот
                        currNode = currNode.parent!!
                        rotateLeft(currNode)
                    }

                    currNode.parent!!.color = COLOR.BLACK
                    currNode.parent!!.parent?.color = COLOR.RED
                    rotateRight(currNode.parent!!.parent!!)
                }
            } else {  //родитель красный
                val uncleNode = currNode.parent!!.parent!!.leftChild

                if (uncleNode != null && uncleNode.color == COLOR.RED) {// + дядя красный

                    uncleNode.parent!!.color = COLOR.RED
                    uncleNode.color = COLOR.BLACK
                    currNode.parent!!.color = COLOR.BLACK
                    currNode = currNode.parent!!.parent ?: break
                } else {// + дядя черный

                    if (currNode == currNode.parent!!.leftChild) {// + новый узел - левый потомок => поворот
                        currNode = currNode.parent!!
                        rotateRight(currNode)
                    }

                    currNode.parent!!.color = COLOR.BLACK
                    currNode.parent!!.parent?.color = COLOR.RED
                    rotateLeft(currNode.parent!!.parent!!)
                }
            }
        }
        root?.color = COLOR.BLACK
    }

    private fun balanceTreeAfterRemove(currNode: RBNode<Key, Data>?, isNullProf: Boolean = false) {

        var isNull = isNullProf
        var node = currNode
        while (isNull || node != root && node!!.color == COLOR.BLACK) {
            //println("isNull = $isNull , node.data = ${node!!.data} , node.parent = ${node.parent}")
            if (node!!.parent!!.leftChild == node || node.parent!!.leftChild == null) {
                var broNode = node.parent!!.rightChild

                //если бро-узел красный, то у него есть два черных потомка => сводим к случаю когда бро-узел черный
                if (broNode!!.color == COLOR.RED) {

                    broNode.color = COLOR.BLACK
                    node.parent!!.color = COLOR.RED
                    rotateLeft(node.parent!!)
                    broNode = node.parent!!.rightChild
                }
                //во всех оставшихся случаех бро-узел черный

                //если оба племянника черные, то меняем цвет бро-узла на красный и скидываем проблему на родителя
                if (!(broNode!!.leftChild != null &&
                        broNode.rightChild != null &&
                        broNode.rightChild!!.color == COLOR.RED &&
                        broNode.leftChild!!.color == COLOR.RED)) {
                    broNode.color = COLOR.RED
                    node = node.parent
                } else {
                    //если левый пельмяш красный, то поворот вправо вокруг бро-узла
                    if (broNode.leftChild != null && broNode.leftChild!!.color == COLOR.RED) {

                        broNode.leftChild!!.color = COLOR.BLACK
                        broNode.color = COLOR.RED
                        rotateRight(broNode)
                    }
                    //последний случай, если правый пельмяшь красный
                    broNode.color = node.parent!!.color
                    node.parent!!.color = COLOR.BLACK
                    broNode.rightChild?.color = COLOR.BLACK
                    rotateLeft(node.parent!!)
                    node = root
                }
            } else  //все тоже самое, только зеркально отраженное leftChild = rightChild
            {       //                                            rotateLeft = rotateRight и наоборот
                var broNode = node.parent!!.leftChild

                if (broNode!!.color == COLOR.RED) {

                    broNode.color = COLOR.BLACK
                    node.parent!!.color = COLOR.RED
                    rotateRight(node.parent!!)
                    broNode = node.parent!!.leftChild
                }

                if (!(broNode!!.leftChild != null &&
                        broNode.rightChild != null &&
                        broNode.rightChild!!.color == COLOR.RED &&
                        broNode.leftChild!!.color == COLOR.RED)) {

                    broNode.color = COLOR.RED
                    node = node.parent
                } else {

                    if (broNode.rightChild != null && broNode.rightChild!!.color == COLOR.RED) {
                        broNode.rightChild!!.color = COLOR.BLACK
                        broNode.color = COLOR.RED
                        rotateLeft(broNode)
                    }

                    broNode.color = node.parent!!.color
                    node.parent!!.color = COLOR.BLACK
                    broNode.leftChild?.color = COLOR.BLACK
                    rotateRight(node.parent!!)
                    node = root
                }
            }
            isNull = false
        }
        node?.color = COLOR.BLACK
    }

    public fun iterator(getNextFun: (RBNode<Key, Data>?) -> RBNode<Key, Data>?): Iterator<RBNode<Key, Data>> {
        getNextNode = getNextFun
        return iterator()
    }

    public override fun iterator(): Iterator<RBNode<Key, Data>> {
        return (object : Iterator<RBNode<Key, Data>> {
            var next = getMax()

            override fun hasNext(): Boolean {
                return next != null
            }

            override fun next(): RBNode<Key, Data> {
                var retNode = next
                next = getNextNode(next)

                return retNode!!
            }
        })
    }


}
