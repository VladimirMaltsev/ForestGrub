package User

import TreeLib.BSTree
import TreeLib.RBTree
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper
import java.io.FileWriter
import java.io.PrintWriter
import java.lang.Math.random
import java.sql.Time

fun main (args : Array<String>) {

    val writer = PrintWriter("analizRandom.txt")
    var n = 10000

    var strBuilderBST = StringBuilder()
    var strBRb = StringBuilder()

    writer.append("Вставка последовательных ключей:\n\n")

    while (n < 60001) {
        var bsTree = BSTree<Int, Int>()
        var rbTree = RBTree<Int, Int>()

        val startBST = System.nanoTime()
        for (i in 0..n) {
            bsTree.insert(i, i)

        }
        println(45)
        val endBST = System.nanoTime()
        val timeBST = endBST - startBST

        val startRB = System.nanoTime()
        for (i in 0..n) {
            rbTree.insert(i, i)
        }
        val endRB = System.nanoTime()
        val timeRB = endRB - startRB

          strBuilderBST.append("$timeBST\n")
        strBRb.append("$timeRB\n")

        println(n)
        n += 5000
    }

//    writer.append("Вставка рандомных ключей:\n\n")
//
//
//
//    while (n < 4000001  ) {
//        val arrRandomKey = Array(n, { _ -> 0})
//        for (i in 0..n - 1)
//        {
//            arrRandomKey[i] = (random() * 100000000).toInt()
//        }
//
//        var bsTree = BSTree<Int, Int>()
//        var rbTree = RBTree<Int, Int>()
//
//        val startBST = System.nanoTime()
//        for (i in 0..n - 1)
//            bsTree.insert(arrRandomKey[i], arrRandomKey[i])
//        val endBST = System.nanoTime()
//        val timeBST = endBST - startBST
//
//        val startRB = System.nanoTime()
//        for (i in 0..n - 1)
//            rbTree.insert(arrRandomKey[i], arrRandomKey[i])
//        val endRB = System.nanoTime()
//        val timeRB = Time(endRB - startRB)
//
//        strBuilderBST.append("$timeBST\n")
//        strBRb.append("$timeRB\n")
//
//        println(n)
//        n += 200000
//    }
    writer.append(strBuilderBST.toString() + "\n\n")
    writer.append(strBRb)
    writer.close()
}