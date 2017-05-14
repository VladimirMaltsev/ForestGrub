package BTreeLib
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class BTreeTest {

    fun isBTreeCorrect (btree : BTree<Int, Int>) : Boolean{
        if (btree.isEmpty() && btree.getRoot().children.size > 0)
            return false

        for (node in btree) {
            if (node.keys.size <= btree.MIN_DIG ||
                    !node.isLeaf() && node.keys.size != node.children.size - 1)
                return false
            for (i in 0..node.keys.size - 2) {
                if (node.keys[i] > node.keys[i + 1])
                    return false
            }
        }

        return true

    }

    fun initTree (size: Int) : BTree<Int, Int> {
        var btree = BTree<Int, Int>()
        for (i in 0..size - 1)
            btree.insert(i, i)

        return btree
    }

    @Test
    fun insert_100000_Nodes() {
        assertEquals(true, isBTreeCorrect(initTree(100000)))
    }

    @Test
    fun isEmpty() {

    }

    @Test
    fun search() {

    }

    @Test
    fun insert() {

    }

    @Test
    fun remove() {

    }



}