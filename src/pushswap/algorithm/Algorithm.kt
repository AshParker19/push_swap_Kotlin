package pushswap.algorithm

import pushswap.models.*
import java.util.ArrayDeque

object Algorithm {

    fun sortMain(store: Store) {
        with (store) {
            when (stackA.size) {
                2 -> sa()
                3 -> sort3(this)
                4, 5 -> sort4Or5(this, stackA.size)
                in 6..9 -> sortLessThan10(this, flag = 0)
                else -> APS.almightyPS(this)
            }
        }
    }

    private fun sort3(store: Store) {
        with(store) {
            if (stackA.first().value > stackA.elementAt(1).value && stackA.first().value > stackA.elementAt(2).index) ra()
            if (stackA.elementAt(1).value > stackA.first().value && stackA.elementAt(1).value > stackA.elementAt(2).value) rra()
            if (stackA.first().value > stackA.elementAt(1).value) sa()
        }
    }

    fun sort4Or5(store: Store, save: Int) {
        with (store) {
            var i = 2

            while (i > 0) {
                smallest = Int.MAX_VALUE
                findTheSmallest(stackA) // TODO: check if can be lambda
                val smallestIndex = stackA.indexOfFirst { it.value == smallest }
                if (smallestIndex <= i) {
                    while (stackA.first().value != smallest) {
                        ra()
                    }
                } else {
                    while (stackA.first().value != smallest) {
                        rra()
                    }
                }
                pb()
                if (save == 4) break
                i--
            }
            sort3(store)
            pa()
            if (save == 5) pa()
        }
    }

    fun sortLessThan10(store: Store, flag: Int) {
        with (store) {
            var i = if (flag == 1) stackA.size - 5 else stackA.size
            val j = i

            while (i > 0) {
                smallest = Int.MAX_VALUE
                findTheSmallest(stackA) // TODO: later check if this is can be done without sending a stack all the time
                val smallestIndex = stackA.indexOfFirst { it.value == smallest }
                rotateUp(smallestIndex, i)
                pb()
                i--
            }
            if (flag == 1) sort4Or5(this,5)
            repeat(j) {
                pa()
            }
        }
    }


    private fun Store.findTheSmallest(stack: ArrayDeque<StackElement>) {
        for ((i, element) in stack.withIndex()) {
            if (element.value < smallest) {
                smallest = element.value
            }
            element.index = i
        }
    }

    private fun Store.rotateUp(smallestIndex: Int, i: Int) {
        if (smallestIndex <= i) {
            while (stackA.first().value != smallest) {
                ra()
            }
        } else {
            while (stackA.first().value != smallest) {
                rra()
            }
        }
    }
}