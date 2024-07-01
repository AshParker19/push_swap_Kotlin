package pushswap.main

import pushswap.models.Store
import pushswap.utils.InputParser
import pushswap.algorithm.Algorithm

fun main(args: Array<String>) {
        val store: Store

        if (args.size == 1) {
                store = InputParser.startFromQuotes(args[0])
        } else {
                InputParser.checker(args)
                store = InputParser.createStoreAndStack(args)
        }
        Algorithm.sortMain(store)
        store.result()
}
