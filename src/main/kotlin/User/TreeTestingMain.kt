package User

import BTreeLib.BTree
import ForestGun.TreeLib.TreePrinter
import TreeLib.BSTree
import TreeLib.RBTree
import TreeLib.Tree
import java.util.*

fun main(args: Array<String>) {
    var tree : Tree<Int, Int>? = null
    val scan = Scanner(System.`in`)

    println("Выберите дерево")
    println("1 - BSTree\n" +
            "2 - RBTree\n" +
            "3 - BTree\n" +
            "0 - exit")

    val type = scan.nextInt()
    when (type) {
        1 -> {
            tree = BSTree<Int, Int> ()
        }
        2 -> {
            tree = RBTree<Int, Int> ()
        }
        3 -> {
            tree = BTree<Int, Int> ()
        }
        else -> return
    }

    println("1 - insert\n" +
            "2 - remove\n" +
            "3 - search\n" +
            "4 - print\n" +
            "0 - exit")

    var d = scan.nextInt()
    while ( d != 0) {
        when (d) {
            1 -> {
                println("количество ключей: ")
                val n = scan.nextInt()
                println("введите значения ключей: ")
                for (i in 0.. n - 1) {
                    val key = scan.nextInt()
                    tree.insert(key, key)
                }
                scan.nextLine()
            }
            2 -> {
                println("удаляемый ключ:")
                val kill = scan.nextInt()
                tree.remove(kill)
            }
            3 -> {
                println("искомый ключ:")
                val ser = scan.nextInt()
                if (tree.search(ser) != null)
                    println("данный ключ найден")
                else
                    println("ключ не найден")
            }
            4 -> {
                when (tree) {
                    is RBTree -> {
                        val treePrinter = TreePrinter<Int, Int>(tree)
                        treePrinter.printTree(2)
                    }
                    is BTree -> {
                        tree.printTree()
                    }
                    is BSTree -> {
                        tree.printPostOrder()
                    }
                }
            }
            else -> return
        }
        d = scan.nextInt()
    }
}