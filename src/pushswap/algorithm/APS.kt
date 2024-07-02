package pushswap.algorithm

import pushswap.models.Store
import pushswap.constants.Constants

// mutable object to imitate C-style call by reference
class DirectionHolder(var direction: Int = 0)

object APS {
    fun almightyPS(store: Store) {
        val copy = IntArray(store.stackA.size)

        store.stackA.forEachIndexed { index, stackElement ->
            copy[index] = stackElement.value
        }
        copy.sort()
        marker(store, copy)

    }

    private fun marker(store: Store, copy: IntArray) {
        val iterator = store.stackA.iterator()

        while (iterator.hasNext()) {
            val node = iterator.next()
            val pos = copy.indexOf(node.value)
            if (pos != -1) {
                val chunkIndex = pos / store.chunkSize + 1
                if (chunkIndex <= store.chunkNum) {
                    node.flag = chunkIndex
                }
            }
        }
    }

    private fun pushBMain(store: Store) {
        var first = 1
        var second = 2
        val pushPair = (store.chunkNum - 2) - 2

        repeat(pushPair) {
            pushPairs(store, first, second, store.chunkSize * 2)
            first += 2
            second += 2
        }
        pushTheRest(store.chunkSize, store.chunkNum - 1)
        pushTheRest(store.chunkSize, store.chunkNum)
    }

    private fun pushPairs(store: Store, bottomFlag: Int, topFlag: Int, count: Int) {
        var rrFlag = false

        repeat (count) {
            store.stackA.forEachIndexed { index, stackElement ->
                stackElement.index = index
            }
            var dir = DirectionHolder()

            val cost = manageDetails(store, bottomFlag, topFlag, dir)
            when {
                store.stackB.isNotEmpty() && dir == Constants.UP && rrFlag && cost > 1 -> manageRR(cost, dir)
                store.stackB.isNotEmpty() && dir == Constants.UP && cost == 1 -> manageTopA(bottomFlag)
                else -> manageRRA(cost, dir, bottomFlag)
            }
            rrFlag = store.stackB.firstOrNull()?.flag == bottomFlag
        }
    }

    private fun manageDetails(store: Store,bottomFlag: Int, topFlag: Int, dir: DirectionHolder): Int {
        val flagCount1 = store.stackA.count { it.flag == bottomFlag}
        val flagCount2 = store.stackA.count { it.flag == topFlag}

        val cost = when {
            flagCount1 > 0 && flagCount2 > 0 -> getDirA(bottomFlag, topFlag, directionHolder)
            flagCount1 > 0 && flagCount2 == 0 -> getDirA(bottomFlag, 0, directionHolder)
            flagCount1 == 0 && flagCount2 > 0 -> getDirA(0, topFlag, directionHolder)
            else -> 0
        }
    }
}

