package pushswap.algorithm

import pushswap.models.StackElement
import pushswap.models.Store
import java.util.ArrayDeque

object APS {
    fun almightyPS(store: Store) {
        val copy = IntArray(store.stackA.size)

        store.stackA.forEachIndexed { index, stackElement ->
            copy[index] = stackElement.value
        }
        copy.sort()
        println(store.stackA.toString())
        marker(store, copy)
        println(store.stackA.toString())

    }

/* I had to comment this recursive function, my old mac cannot handle it
    private fun marker(store: Store, copy: IntArray, chunkIndex: Int) {
        if (store.stackA.isEmpty()) return

        val pos = copy.indexOf(store.stackA.first().value)
        val rangeStart = (chunkIndex - 1) * store.chunkSize
        val rangeEnd = chunkIndex * store.chunkSize

        if (pos in rangeStart..rangeEnd) {
            store.stackA.first().flag = chunkIndex
        }

        val firstElement = store.stackA.removeFirst()
        marker(store, copy, chunkIndex)
        store.stackA.addLast(firstElement)

        if (chunkIndex < store.chunkNum) {
            marker(store, copy, chunkIndex + 1)
        }
    }
*/

    private fun marker(store: Store, copy: IntArray) {
        var chunkIndex = 1

        while (chunkIndex <= store.chunkNum) {
            if (store.stackA.isEmpty()) return

            val pos = copy.indexOf(store.stackA.first().value)
            val rangeStart = (chunkIndex - 1) * store.chunkSize
            val rangeEnd = chunkIndex * store.chunkSize

            if (pos in rangeStart until rangeEnd) {
                store.stackA.first().flag = chunkIndex
            }

            val firstElement = store.stackA.removeFirst()
            store.stackA.addLast(firstElement)

            chunkIndex++
        }

    }

//    private fun pushBMain(store: Store) {
//
//    }
}

