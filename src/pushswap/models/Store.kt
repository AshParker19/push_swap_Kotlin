package pushswap.models

import java.util.ArrayDeque

class Store( // primary constructor

    // data needed for the algorithm
    var countA: Int = 0,
    var countB: Int = 0,
    var chunkSize: Int = 0,
    var chunkNum: Int = 0,
    var biggest: Int = Int.MIN_VALUE,
    var sndBiggest: Int = Int.MIN_VALUE,
    var smallest: Int = Int.MAX_VALUE,

    // lists which represent stacks A and B
    var stackA: ArrayDeque<StackElement> = ArrayDeque(),
    var stackB: ArrayDeque<StackElement> = ArrayDeque()
) {
    fun sa() {
        if (stackA.size > 1) {
            val first = stackA.removeFirst()
            val second = stackA.removeFirst()
            stackA.addFirst(first)
            stackA.addFirst(second)
            println("sa")
        }
    }

    fun sb() {
        if (stackB.size > 1) {
            val first = stackB.removeFirst()
            val second = stackB.removeFirst()
            stackB.addFirst(first)
            stackB.addFirst(second)
            println("sb")
        }
    }

    fun ss() {
        sa()
        sb()
        println("ss")
    }

    fun pa() {
        if (stackB.isNotEmpty()) {
            stackA.addFirst(stackB.removeFirst())
            countA++
            countB--
            println("pa")
        }
    }

    fun pa(value: Int) {
        stackA.addFirst(StackElement(value))
    }

    fun pb() {
        if (stackA.isNotEmpty()) {
            stackB.addFirst(stackA.removeFirst())
            countA--
            countB++
            println("pb")
        }
    }

    fun ra() {
        if (stackA.size > 1) {
            stackA.addLast(stackA.removeFirst())
            println("ra")
        }
    }

    fun rb() {
        if (stackB.size > 1) {
            stackB.addLast(stackB.removeFirst())
            println("rb")
        }
    }

    fun rr() {
        ra()
        rb()
        println("rr")
    }

    fun rra() {
        if (stackA.size > 1) {
            stackA.addFirst(stackA.removeLast())
            println("rra")
        }
    }

    fun rrb() {
        if (stackB.size > 1) {
            stackB.addFirst(stackB.removeLast())
            println("rrb")
        }
    }

    fun rrr() {
        rra()
        rrb()
        println("rrr")
    }
}