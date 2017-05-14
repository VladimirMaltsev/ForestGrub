package User

import ForestGun.TreeLib.TreePrinter
import TreeLib.BSTree
import TreeLib.RBTree
import java.util.*

fun main(args: Array<String>) {
    var rbTree : RBTree<Int, Int> = RBTree();
    val treePrinter = TreePrinter<Int, Int>(rbTree)
    val scan = Scanner(System.`in`)

    println("1 - insert in\n" +
            "2 - remove from\n" +
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
                    var key = scan.nextInt()
                    rbTree.insert(key, key)
                }
            }
            2 -> {
                println("удаляемый ключ:")
                val kill = scan.nextInt()
                rbTree.remove(kill)
            }
            3 -> {
                println("искомый ключ:")
                val ser = scan.nextInt()
                if (rbTree.search(ser) != null)
                    println("данный ключ найден")
                else
                    println("ключ не найден")
            }
            4 -> {
                treePrinter.printTree(2)
            }
            else ->
                    return
        }
        d = scan.nextInt()
    }

    treePrinter.printTree(2)
}