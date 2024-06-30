package pushswap.utils

import pushswap.constants.Constants
import pushswap.models.*

object InputParser {

    fun startFromQuotes(input: String, store: Store) {
        require(input.isNotEmpty()) { "Error: Input cannot be empty" }

        val splitInput = input.split(" \t")
        require(splitInput.all { it.isNumeric() }) { "Error: All elements must be numeric" }

        checker(splitInput.toTypedArray())
        createStack(splitInput.toTypedArray(), store)
    }

    fun checker(args: Array<String>) {
        require(args.isNotEmpty()) { "Error: Arguments cannot be empty "}

        args.forEach { arg ->
            require(arg.isNumeric()) { "Error: Argument '$arg' is not numeric" }
        }

        val numbers = args.map { it.toInt() }
        require(numbers.distinct().size == numbers.size) { "Error: Duplicate values found " }
        require(numbers.all { it in Int.MIN_VALUE..Int.MAX_VALUE }) { "Error: Values must be in the range of INT_MIN and INT_MAX"} // is this already checked?
        require(!numbers.isSorted()) { "Error: Values are already sorted" }
    }

    fun createStack(args: Array<String>, store: Store) {
        val numbers = args.map { it.toInt() }
        numbers.forEachIndexed { index, value ->
            store.pushA(StackElement(value, index, Constants.NEUTRAL))
        }
    }

    /*
        extension function for String class.
        it's private because it's called ?
    */
    private fun String.isNumeric(): Boolean {
        return this.toIntOrNull() != null
    }

    private fun List<Int>.isSorted(): Boolean {
        for (i in 0 until this.size - 1) {
            if (this[i] > this[i + 1]) {
                return false
            }
        }
        return true
    }
}