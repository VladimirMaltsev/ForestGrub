import ForestGun.TreeLib.COLOR
import ForestGun.TreeLib.TreePrinter
import ForestGun.TreeUtils.getBlackHeightOf
import ForestGun.TreeUtils.getMax
import TreeLib.RBTree
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class RBTreeTest {


    @Test
    fun insert100000() {
        var rbTree = initTree(100000)
        // TreePrinter<Int, Int>(rbTree).printTree(2)
        assertEquals(true, isTreeRBTree(rbTree))
    }

    @Test
    fun searchByExistingKey() {
        var rbTree = initTree(10)
        assertEquals(6, rbTree.search(6))
    }

    @Test
    fun removeByExistingKey() {
        var rbTree = initTree(10)
        rbTree.remove(5)
    }

    @Test
    fun searchByNotExistingKey() {
        var rbTree = initTree(10)
        rbTree.search(15)
    }

    @Test
    fun removeByNotExistingKey() {
        var rbTree = initTree(10)
        rbTree.remove(15)
    }

    @Test
    fun searchInEmptyTree() {
        var rbTree = RBTree<Int, Int>()
        rbTree.search(10)
    }

    @Test
    fun removeFromEmptyTree() {
        var rbTree = RBTree<Int, Int>()
        rbTree.remove(10)
    }

    fun initTree(size : Int) : RBTree<Int, Int>{
        var rbTree = RBTree<Int, Int>()
        for (i in 1..size) {
            rbTree.insert(i, i)
        }
        return rbTree
    }

    fun isTreeRBTree(rbTree: RBTree<Int, Int>) : Boolean{
        if (rbTree.root != null && rbTree.root!!.color == COLOR.RED)
            return false

        var blackHeight = rbTree.getBlackHeightOf(rbTree.getMax())
        for (node in rbTree) {


            if (node.rightChild == null && node.leftChild == null
                    && rbTree.getBlackHeightOf(node) != blackHeight) {
                return false
            }


            if (node.parent != null && node.color == COLOR.RED && node.parent!!.color == COLOR.RED)
                return false
        }

        return true
    }

}