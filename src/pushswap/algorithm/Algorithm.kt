package pushswap.algorithm

import pushswap.models.*
import java.util.ArrayDeque

object Algorithm {

    fun sortMain(store: Store) {

        when (store.stackA.size) {
            2 -> store.sa()
            3 -> sort3(store)
            4, 5 -> sort4Or5(store, store.stackA.size)
            in 6..9 -> sortLessThan10(store, flag = 0)
            else -> APS.almightyPS(store)
        }
    }

    private fun sort3(store: Store) {
        with(store) {
            if (stackA.first().value > stackA.elementAt(1).value && stackA.first().value > stackA.elementAt(2).value) ra()
            if (stackA.elementAt(1).value > stackA.first().value && stackA.elementAt(1).value > stackA.elementAt(2).value) rra()
            if (stackA.first().value > stackA.elementAt(1).value) sa()
        }
    }

    fun sort4Or5(store: Store, save: Int) {
        var i = 2

        while (i > 0) {
            store.smallest = Int.MAX_VALUE
            findTheSmallest(store, store.stackA)
            val smallestIndex = store.stackA.indexOfFirst { it.value == store.smallest }
            if (smallestIndex <= i) {
                while (store.stackA.first().value != store.smallest) {
                    store.ra()
                }
            } else {
                while (store.stackA.first().value != store.smallest) {
                    store.rra()
                }
            }
            store.pb()
            if (save == 4) break
            i--
        }
        sort3(store)
        store.pa()
        if (save == 5) store.pa()
    }

     fun sortLessThan10(store: Store, flag: Int) {
        var i = if (flag == 1) store.stackA.size - 5 else store.stackA.size
        val j = i

        while (i > 0) {
            store.smallest = Int.MAX_VALUE
            findTheSmallest(store, store.stackA) // TODO: later check if this is can be done without sending a stack all the time
            val smallesIndex = store.stackA.indexOfFirst { it.value == store.smallest }
            rotateUp(store, smallesIndex, i)
            store.pb()
            i--
        }
        if (flag == 1) sort4Or5(store, 5)
        repeat(j) {
            store.pa()
        }
    }

    fun findTheSmallest(store: Store, stack: ArrayDeque<StackElement>) {
        for ((i, element) in stack.withIndex()) {
            if (element.value < store.smallest) {
                store.smallest = element.value
            }
            element.index = i
        }
    }

    private fun rotateUp(store: Store, smallestIndex: Int, i: Int) {
        if (smallestIndex <= i) {
            while (store.stackA.first().value != store.smallest) {
                store.ra()
            }
        } else {
            while (store.stackA.first().value != store.smallest) {
                store.rra()
            }
        }
    }
}