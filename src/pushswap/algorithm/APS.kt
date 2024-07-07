package pushswap.algorithm

import pushswap.models.Store
import pushswap.constants.Constants

// mutable object to imitate C-style call by reference
data class DirectionHolder(var direction: Int)

object APS { //TODO check is I can apply some scope function for the whole class so I don't next to pass Store around
    fun almightyPS(store: Store) {
        val copy = IntArray(store.stackA.size)

        store.stackA.forEachIndexed { index, stackElement ->
            copy[index] = stackElement.value
        }
        copy.sort()
        marker(store, copy)
        pushBMain(store)
        sortRemainder(store)
        pushAMain(store)
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
        val pushPair = (store.chunkNum - 2) / 2

        repeat(pushPair) {
            pushPairs(store, first, second, store.chunkSize * 2)
            first += 2
            second += 2
        }
        pushTheRest(store, store.chunkSize, store.chunkNum - 1)
        pushTheRest(store, store.chunkSize, store.chunkNum)
    }

    private fun pushPairs(store: Store, bottomFlag: Int, topFlag: Int, count: Int) {
        var rrFlag = false

        repeat (count) {
            store.stackA.forEachIndexed { index, stackElement ->
                stackElement.index = index
            }
            val dir = DirectionHolder(Constants.UP)

            val cost = manageDetails(store, bottomFlag, topFlag, dir)
            when {
                store.stackB.isNotEmpty() && dir.direction == Constants.UP && rrFlag && cost > 1 -> manageRR(store, cost, dir)
                store.stackB.isNotEmpty() && dir.direction == Constants.UP && cost == 1 -> manageTopA(store, bottomFlag)
                else -> manageRRA(store, cost, dir, bottomFlag)
            }
            rrFlag = store.stackB.firstOrNull()?.flag == bottomFlag
        }
    }

    private fun manageDetails(store: Store,bottomFlag: Int, topFlag: Int, dir: DirectionHolder): Int {
        val flagCount1 = store.stackA.count { it.flag == bottomFlag}
        val flagCount2 = store.stackA.count { it.flag == topFlag}

        return when {
            flagCount1 > 0 && flagCount2 > 0 -> getDirA(store, bottomFlag, topFlag, dir)
            flagCount1 > 0 && flagCount2 == 0 -> getDirA(store, bottomFlag, 0, dir)
            flagCount1 == 0 && flagCount2 > 0 -> getDirA(store, 0, topFlag, dir)
            else -> 0
        }
    }

    fun getDirA(store: Store, flag1: Int, flag2: Int, dir: DirectionHolder): Int {
        return if (flag1 != 0 && flag2 != 0) {
            val flag1Cost = findCloser(store, flag1, dir)
            val dirSave = dir.direction
            val flag2Cost = findCloser(store, flag2, dir)
            if (flag1Cost <= flag2Cost) {
                dir.direction = dirSave
                flag1Cost
            } else {
                flag2Cost
            }
        } else if (flag1 != 0) {
            findCloser(store, flag1, dir)
        } else {
            findCloser(store, flag2, dir)
        }
    }

    fun findCloser(store: Store, flag: Int, dir: DirectionHolder): Int {
        val indexFromStart = store.stackA.indexOfFirst { element ->
            element.flag == flag
        }

        val indexFromEnd = store.stackA.indexOfLast { element ->
            element.flag == flag
        }

        val startCost = findCost(indexFromStart, store.stackA.size, dir)
        val dirSave = dir.direction
        val endCost = findCost(indexFromEnd, store.stackA.size, dir)

        return if (startCost <= endCost) {
            dir.direction = dirSave
            startCost
        } else {
            endCost
        }
    }

    fun findCost(index: Int, count: Int, dir: DirectionHolder): Int {
        return if (index <= count - index) {
            dir.direction = Constants.UP
            index + 1
        } else {
            dir.direction = Constants.DOWN
            count - index + 1
        }
    }

    fun manageRR(store: Store, cost: Int, dir: DirectionHolder) {
        store.rr()
        store.rotateStack(cost - 1, dir, Constants.STACK_A)
        store.pb()
    }

    fun manageTopA(store: Store, bottomFlag: Int) {
        if (store.stackB.first.flag == bottomFlag || store.stackB.first.flag == bottomFlag - 2) {
            store.rb()
        }
        store.pb()
    }

    fun manageRRA(store: Store, cost: Int, dir: DirectionHolder, bottomFlag: Int) {
        if (store.stackB.isNotEmpty() && (store.stackB.first.flag == bottomFlag
                    || store.stackB.first.flag == bottomFlag - 2)) {
            store.rb()
        }
        store.rotateStack(cost, dir, Constants.STACK_A)
        store.pb()
    }

    fun pushTheRest(store: Store, count: Int, chunkIndex: Int) {
        val chunkSum = store.stackA.filter { it.flag == chunkIndex }
            .sumOf { it.value }

        val mean = chunkSum / store.chunkSize
        var rrFlag = false

        repeat(count) {
            store.stackA.forEachIndexed { index, stackElement ->
                stackElement.index = index
            }
            val dir = DirectionHolder(Constants.UP)
            val cost = getDirA(store, chunkIndex, 0, dir)
            if (dir.direction == Constants.UP && rrFlag && cost > 1) {
                manageRR(store, cost, dir)
            } else if (dir.direction == Constants.UP && cost == 1) {
                if (store.stackB.first.value < mean) {
                    store.rb()
                }
                store.pb()
            } else {
                manageRRA2(store, cost, dir, mean)
            }
            if (store.stackB.first.value < mean) {
                rrFlag = true
            } else {
                rrFlag = false
            }
        }
    }

    fun manageRRA2(store: Store, cost: Int, dir: DirectionHolder, mean: Int) {
        if (store.stackB.first.value < mean
            && (store.stackB.first.flag == store.chunkNum - 1)) {
            store.rb()
        }
        store.rotateStack(cost, dir, Constants.STACK_A)
        store.pb()
    }

    fun sortRemainder(store: Store) {
        if (store.stackA.size == 4) {
            Algorithm.sort4Or5(store, 4)
        } else if (store.stackA.size == 10) {
            Algorithm.sortLessThan10(store, 1)
        } else {
            Algorithm.sortLessThan10(store, 0)
        }
    }

    // 0, 1, 0
    fun pushAMain(store: Store) {
        while (store.stackB.isNotEmpty()) {
            store.biggest = Int.MIN_VALUE
            find1st2nd(store)
            var cost = findCost2
        }
    }

    fun find1st2nd(store: Store) {
        store.stackB.forEachIndexed { index, stackElement ->
            when {
                stackElement.value > store.biggest -> {
                    store.sndBiggest = store.biggest
                    store.biggest = stackElement.value
                }
                stackElement.value > store.sndBiggest -> {
                    store.sndBiggest = stackElement.value
                }
            }
            stackElement.index = index
        }
    }

    fun findCost2()
}

