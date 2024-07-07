package pushswap.algorithm

import pushswap.models.Store
import pushswap.constants.Constants

// mutable object to imitate C-style call by reference
data class Holder(var direction: Int = 0, var toPush: Int = 0, var count: Int = 0)

object APS {
    fun almightyPS(store: Store) {
        with (store) {
            val copy = IntArray(store.stackA.size)
            store.stackA.forEachIndexed { index, stackElement ->
                copy[index] = stackElement.value
            }

            copy.sort()
            marker(copy)
            pushBMain()
            sortRemainder()
            pushAMain()
        }
    }

    private fun Store.marker(copy: IntArray) {
        val iterator = stackA.iterator()

        while (iterator.hasNext()) {
            val node = iterator.next()
            val pos = copy.indexOf(node.value)
            if (pos != -1) {
                val chunkIndex = pos / chunkSize + 1
                if (chunkIndex <= chunkNum) {
                    node.flag = chunkIndex
                }
            }
        }
    }

    private fun Store.pushBMain() {
        var first = 1
        var second = 2
        val pushPair = (chunkNum - 2) / 2

        repeat(pushPair) {
            pushPairs(first, second, chunkSize * 2)
            first += 2
            second += 2
        }
        pushTheRest(chunkSize, chunkNum - 1)
        pushTheRest(chunkSize, chunkNum)
    }

    private fun Store.pushPairs(bottomFlag: Int, topFlag: Int, count: Int) {
        var rrFlag: Boolean? = null

        repeat (count) {
            stackA.forEachIndexed { index, stackElement ->
                stackElement.index = index
            }
            val dir = Holder()

            val cost = manageDetails(bottomFlag, topFlag, dir)
            when {
                stackB.isNotEmpty() && dir.direction == Constants.UP && (rrFlag == true || rrFlag == null) && cost > 1 -> manageRR(cost, dir)
                stackB.isNotEmpty() && dir.direction == Constants.UP && cost == 1 -> manageTopA(bottomFlag)
                else -> manageRRA(cost, dir, bottomFlag)
            }
            rrFlag = stackB.firstOrNull()?.flag == bottomFlag
        }
    }

    private fun Store.manageDetails(bottomFlag: Int, topFlag: Int, dir: Holder): Int {
        val flagCount1 = stackA.count { it.flag == bottomFlag}
        val flagCount2 = stackA.count { it.flag == topFlag}

        return when {
            flagCount1 > 0 && flagCount2 > 0 -> getDirA(bottomFlag, topFlag, dir)
            flagCount1 > 0 && flagCount2 == 0 -> getDirA(bottomFlag, 0, dir)
            flagCount1 == 0 && flagCount2 > 0 -> getDirA(0, topFlag, dir)
            else -> 0
        }
    }

    private fun Store.getDirA(flag1: Int, flag2: Int, dir: Holder): Int {
        return if (flag1 != 0 && flag2 != 0) {
            val flag1Cost = findCloser(flag1, dir)
            val dirSave = dir.direction
            val flag2Cost = findCloser(flag2, dir)
            if (flag1Cost <= flag2Cost) {
                dir.direction = dirSave
                flag1Cost
            } else {
                flag2Cost
            }
        } else if (flag1 != 0) {
            findCloser(flag1, dir)
        } else {
            findCloser(flag2, dir)
        }
    }

    private fun Store.findCloser(flag: Int, dir: Holder): Int {
        val indexFromStart = stackA.indexOfFirst { element ->
            element.flag == flag
        }
        val indexFromEnd = stackA.indexOfLast { element ->
            element.flag == flag
        }

        val startCost = findCost(indexFromStart, stackA.size, dir)
        val dirSave = dir.direction
        val endCost = findCost(indexFromEnd, stackA.size, dir)

        return if (startCost <= endCost) {
            dir.direction = dirSave
            startCost
        } else {
            endCost
        }
    }

    private fun findCost(index: Int, count: Int, dir: Holder): Int {
        return if (index <= count - index) {
            dir.direction = Constants.UP
            index + 1
        } else {
            dir.direction = Constants.DOWN
            count - index + 1
        }
    }

    private fun Store.manageRR(cost: Int, dir: Holder) {
        rr()
        rotateStack(cost - 1, dir, Constants.STACK_A)
        pb()
    }

    private fun Store.manageTopA(bottomFlag: Int) {
        if (stackB.first.flag == bottomFlag || stackB.first.flag == bottomFlag - 2) {
            rb()
        }
        pb()
    }

    private fun Store.manageRRA(cost: Int, dir: Holder, bottomFlag: Int) {
        if (stackB.isNotEmpty() && (stackB.first.flag == bottomFlag
                    || stackB.first.flag == bottomFlag - 2)) {
            rb()
        }
        rotateStack(cost, dir, Constants.STACK_A)
        pb()
    }

    private fun Store.pushTheRest(count: Int, chunkIndex: Int) {
        val chunkSum = stackA.filter { it.flag == chunkIndex }
            .sumOf { it.value }

        val mean = chunkSum / chunkSize
        var rrFlag = false

        repeat(count) {
            stackA.forEachIndexed { index, stackElement ->
                stackElement.index = index
            }
            val dir = Holder(Constants.UP)
            val cost = getDirA(chunkIndex, 0, dir)
            if (dir.direction == Constants.UP && rrFlag && cost > 1) {
                manageRR(cost, dir)
            } else if (dir.direction == Constants.UP && cost == 1) {
                if (stackB.first.value < mean) {
                    rb()
                }
                pb()
            } else {
                manageRRA2(cost, dir, mean)
            }
            rrFlag = stackB.first.value < mean
        }
    }

    private fun Store.manageRRA2(cost: Int, dir: Holder, mean: Int) {
        if (stackB.first.value < mean
            && (stackB.first.flag == chunkNum - 1)) {
            rb()
        }
        rotateStack(cost, dir, Constants.STACK_A)
        pb()
    }

    private fun Store.sortRemainder() {
        when (stackA.size) {
            4 -> Algorithm.sort4Or5(this, 4)
            10 -> Algorithm.sortLessThan10(this, 1)
            else -> Algorithm.sortLessThan10(this, 0)
        }
    }

    private fun Store.pushAMain() {
        val dir = Holder()
        var first = 1
        var wasPushed = 0

        while (stackB.isNotEmpty()) {
            biggest = Int.MIN_VALUE
            find1st2nd()
            val cost = findCost2(dir)
            rotateStack(cost, dir, Constants.STACK_B)
            if (manageSB()) {
                continue
            }
            if (first-- == 1) {
                pa()
            } else if (wasPushed == Constants.BIGGEST) {
                pa()
            } else if (wasPushed == Constants.SND_BIGGEST) {
                manageSndBiggest(dir)
            }
            wasPushed = dir.toPush
        }
    }

    private fun Store.find1st2nd() {
        stackB.forEachIndexed { index, stackElement ->
            when {
                stackElement.value > biggest -> {
                    sndBiggest = biggest
                    biggest = stackElement.value
                }
                stackElement.value > sndBiggest -> {
                    sndBiggest = stackElement.value
                }
            }
            stackElement.index = index
        }
    }

    private fun Store.findCost2(dir: Holder): Int {
        var position = stackB.indexOfFirst { element ->
            element.value == biggest
        }.coerceAtLeast(0)
        val cost1 = findCost(position, stackB.size, dir)
        val dirCopy = dir.direction

        position = stackB.indexOfFirst { element ->
            element.value == sndBiggest
        }.coerceAtLeast(0)
        val cost2 = findCost(position, stackB.size, dir)

        return if (cost1 <= cost2) {
            dir.toPush = Constants.BIGGEST
            dir.direction = dirCopy
            cost1
        } else {
            dir.toPush = Constants.SND_BIGGEST
            cost2
        }
    }

    private fun Store.manageSB(): Boolean {
        return if (stackB.first.value == sndBiggest
            && stackB.elementAt(1).value == biggest) {
            sb()
            true
        } else {
            false
        }
    }

    private fun Store.manageSndBiggest(dir: Holder) {
        if (dir.count == 0) {
            dir.count++
        }
        when (dir.toPush) {
            Constants.BIGGEST -> {
                rollback(dir.count)
                dir.count = 0
            }
            Constants.SND_BIGGEST -> {
                pa()
                dir.count++
            }
        }
    }

    private fun Store.rollback(count: Int) {
        when (count) {
            1 -> {
                pa()
                sa()
            }
            else -> {
                repeat(count) {
                    ra()
                }
                pa()
                repeat(count) {
                    rra()
                }
            }
        }
    }
}

