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
        marker(store, copy, 1)


    }

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

}

