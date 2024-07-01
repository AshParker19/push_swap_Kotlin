package pushswap.utils

import pushswap.models.*

object InputParser {

    fun startFromQuotes(input: String): Store {
        require(input.isNotEmpty()) { "Error: Input cannot be empty" }

        val splitInput = input.split(" ")
        require(splitInput.all { it.isNumeric() }) { "Error: All elements must be numeric" }

        checker(splitInput.toTypedArray())

        return createStoreAndStack(splitInput.toTypedArray())
    }

    fun checker(args: Array<String>) {
        require(args.isNotEmpty()) { "Error: Arguments cannot be empty "}

        args.forEach { arg ->
            require(arg.isNumeric()) { "Error: Argument '$arg' is not numeric" }
        }
        require(args.size > 1) { "Error: Need more than 1 value "}

        val numbers = args.map { it.toInt() }
        require(numbers.distinct().size == numbers.size) { "Error: Duplicate values found " }
        // this check is redundant because it's already validated is
        // require(numbers.all { it in Int.MIN_VALUE . Int.MAX_VALUE }) { "Error: Values must be in the range of INT_MIN and INT_MAX"}
        require(!numbers.isSorted()) { "Error: Values are already sorted" }
    }

    fun createStoreAndStack(args: Array<String>): Store {
        val store = Store(inputSize = args.size)
        val numbers = args.map { it.toInt() }

        numbers.forEach { value ->
            store.pa(value)
        }

        return store
    }

    /*
        extension function for String class.
        it's private because it's called only withing this object
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