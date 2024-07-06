package pushswap.models

import pushswap.constants.Constants
import pushswap.algorithm.DirectionHolder
import java.util.ArrayDeque

class Store(inputSize: Int)
{
    // data needed for the algorithm
    val chunkSize: Int
    val chunkNum: Int
    var biggest: Int = Int.MIN_VALUE
    var sndBiggest: Int = Int.MIN_VALUE
    var smallest: Int = Int.MAX_VALUE

    var totalMoves: Int = 0

    // deques which represent stacks A and B
    var stackA: ArrayDeque<StackElement> = ArrayDeque()
    var stackB: ArrayDeque<StackElement> = ArrayDeque() //TODO make this variables private

    init {
        when (inputSize) {
            100 -> {
                chunkSize = 12
                chunkNum = 8
            }
            500 -> {
                chunkSize = 35
                chunkNum = 14
            }
            else -> {
                chunkSize = inputSize / 10
                chunkNum = 10
            }
        }
    }

    // stack operations
    fun sa() {
        if (stackA.size > 1) {
            val first = stackA.removeFirst()
            val second = stackA.removeFirst()
            stackA.addFirst(first)
            stackA.addFirst(second)
            totalMoves++
            println("sa")
        }
    }

    fun sb() {
        if (stackB.size > 1) {
            val first = stackB.removeFirst()
            val second = stackB.removeFirst()
            stackB.addFirst(first)
            stackB.addFirst(second)
            totalMoves++
            println("sb")
        }
    }

    fun ss() {
        sa()
        sb()
        totalMoves++
        println("ss")
    }

    fun pa() {
        if (stackB.isNotEmpty()) {
            stackA.addFirst(stackB.removeFirst())
            totalMoves++
            println("pa")
        }
    }

    fun pa(value: Int) {
        stackA.addLast(StackElement(value))
    }

    fun pb() {
        if (stackA.isNotEmpty()) {
            stackB.addFirst(stackA.removeFirst())
            totalMoves++
            println("pb")
        }
    }

    fun ra() {
        if (stackA.size > 1) {
            stackA.addLast(stackA.removeFirst())
            totalMoves++
            println("ra")
        }
    }

    fun rb() {
        if (stackB.size > 1) {
            stackB.addLast(stackB.removeFirst())
            totalMoves++
            println("rb")
        }
    }

    fun rr() {
        ra()
        rb()
        totalMoves++
        println("rr")
    }

    fun rra() {
        if (stackA.size > 1) {
            stackA.addFirst(stackA.removeLast())
            totalMoves++
            println("rra")
        }
    }

    fun rrb() {
        if (stackB.size > 1) {
            stackB.addFirst(stackB.removeLast())
            totalMoves++
            println("rrb")
        }
    }

    fun rrr() {
        rra()
        rrb()
        totalMoves++
        println("rrr")
    }

    // utils
    private fun isSorted() {
        val iterator = stackA.iterator()
        var current = iterator.next()

        while (iterator.hasNext()) {
            val next = iterator.next()
            if (current.value > next.value) {
                println("KO")
                return
            }
            current = next
        }
        println("OK")
    }

    fun rotateStack(cost: Int, dir: DirectionHolder, stackFlag: Int) {
        repeat(cost - 1) {
            when (stackFlag) {
                Constants.STACK_A -> when (dir.direction) {
                    Constants.UP -> ra()
                    Constants.DOWN -> rra()
                }
                Constants.STACK_B -> when (dir.direction) {
                    Constants.UP -> rb()
                    Constants.DOWN -> rrb()
                }
            }
        }
    }

    fun result() {
        println("================================")
        isSorted()
        println("Number of elements    --> ${stackA.size}")
        println("Total number of moves --> $totalMoves")
        println("================================")

    }
}