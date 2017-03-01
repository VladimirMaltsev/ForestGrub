package TreeLib

import java.util.*

class BSTree<Key : Comparable<Key>, Data> (var root: BSTNode<Key, Data>? = null) : Tree<Key, Data> {

    override fun insert(key: Key, data: Data) {
        var newNode: BSTNode<Key, Data> = BSTNode(key, data)
        if (root == null) {
            root = newNode
            return
        }
        var currNode: BSTNode<Key, Data>? = root

        while (currNode != null) {
            if (currNode.key > newNode.key) {
                if (currNode.leftChild == null) {
                    currNode.leftChild = newNode
                    newNode.parent = currNode
                    return
                }
                currNode = currNode.leftChild;
            } else {
                if (currNode.rightChild == null) {
                    currNode.rightChild = newNode
                    newNode.parent = currNode
                    return
                }
                currNode = currNode.rightChild
            }
        }
        return
    }

    override fun search(key: Key): Data? {
        var currNode: BSTNode<Key, Data>? = root

        while (currNode != null) {
            if (currNode.key == key)
                return currNode.data

            if (currNode.key > key)
                currNode = currNode.leftChild
            else
                currNode = currNode.rightChild
        }

        return null
    }

    override fun remove(key: Key): Boolean
    {
        var victimNode = root

        //поиск узла-жертвы с заданным ключом
        while (victimNode != null && victimNode.key != key)
        victimNode = if (victimNode.key > key) victimNode.leftChild else victimNode.rightChild

        //узел не найден
        if (victimNode == null) return false;

        //определение узла, который будет удален
        var deletedNode: BSTNode<Key, Data>? = victimNode
        if ((deletedNode!!.leftChild != null) && (deletedNode.rightChild != null))
        {
            deletedNode = victimNode.rightChild
            while (deletedNode!!.leftChild != null)
                deletedNode = deletedNode.leftChild
        }

        //узел, который встанет на место удаленного узла
        var succNode = deletedNode.leftChild ?: deletedNode.rightChild

        //удаляем deletedNode
        succNode?.parent = deletedNode.parent

        if (deletedNode.parent != null)
        if (deletedNode.parent!!.leftChild == deletedNode)
        deletedNode.parent!!.leftChild = succNode
        else
        deletedNode.parent!!.rightChild = succNode
        else
        root = succNode

        if (deletedNode != victimNode)
        {
            victimNode.key = deletedNode.key
            victimNode.data = deletedNode.data
        }

    //    if (!deletedNode.isRed)
    //    balanceTreeAfterRemove(succNode)

        return true
    }
//    {
//        var needNode = root
//        while (needNode != null && needNode.key != key) {
//            if (needNode.key > key) needNode = needNode.leftChild else needNode = needNode.rightChild
//        }
//        if (needNode == null) return null; //узел не найден
//
//        if ((needNode.leftChild == null) || (needNode.rightChild == null))
//        { // если не более одного потомка
//            if (needNode.parent == null) {
//                root = root!!.leftChild ?: root!!.rightChild
//                root?.parent = null;
//                return root;
//            }
//
//            if (needNode.parent!!.key > key)
//            {
//                needNode.parent!!.leftChild = needNode.leftChild ?: needNode.rightChild
//                needNode.leftChild?.parent = needNode.parent
//                needNode.rightChild?.parent = needNode.parent
//            } else
//            {
//                needNode.parent!!.rightChild = needNode.leftChild ?: needNode.rightChild
//                needNode.leftChild?.parent = needNode.parent
//                needNode.rightChild?.parent = needNode.parent
//            }
//            return (needNode.leftChild ?: needNode.rightChild)
//        }
//
//        //поиск преемника
//        var heirNode = needNode.rightChild
//        while (heirNode!!.leftChild != null)
//            heirNode = heirNode.leftChild
//
//        needNode.key = heirNode.key
//        needNode.data = heirNode.data
//        //needNode.isRed = heirNode.isRed
//
//        if (heirNode == needNode.rightChild)
//        {
//            needNode.rightChild = heirNode.rightChild
//            heirNode.rightChild?.parent = needNode;
//
//        } else
//        {
//            heirNode.parent!!.leftChild = heirNode.rightChild
//            heirNode.rightChild?.parent = heirNode.parent
//        }
//
//        return needNode
//    }

    fun printPostOrder(curNode: BSTNode<Key, Data>? = root, divider: String = "")
    {
        if (curNode == null)
            return
        var spec_symbol = "";
        if (curNode.parent != null)
            if (curNode == curNode.parent!!.leftChild)
                spec_symbol = "\\"
            else
                spec_symbol = "/"

        printPostOrder(curNode.rightChild, divider + "   ")
        println("$divider$spec_symbol${curNode.data}")
        printPostOrder(curNode.leftChild, divider + "   ")
    }

    fun printLevelOrder()
    {
        if (root == null) {
            println("TreeLib.Tree is empty"); return
        }

        var queueNodes: Queue<BSTNode<Key, Data>> = LinkedList<BSTNode<Key, Data>>()
        var queueChars: Queue<String> = LinkedList<String>()

        var special: String = "\n"
        var plain: String = " "

        queueNodes.add(root)
        queueChars.add(special)

        while (!queueNodes.isEmpty())
        {
            var currNode = queueNodes.poll()

            print("${currNode.data}${queueChars.peek()}")

            if (currNode.leftChild != null) queueNodes.add(currNode.leftChild)
            if (currNode.rightChild != null) queueNodes.add(currNode.rightChild)

            if (queueChars.poll().compareTo("\n") == 0)
            {
                plain += " "
                special = "\n"
            } else special = plain

            if (currNode.leftChild != null && currNode.rightChild != null)
            {
                queueChars.add(plain)
                queueChars.add(special)
            } else
            {
                if (currNode.leftChild != null || currNode.rightChild != null)
                    queueChars.add(plain + special)
                else
                    queueChars.add(plain + special)
            }

        }
    }

}