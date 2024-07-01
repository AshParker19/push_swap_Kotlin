package pushswap.main

import pushswap.models.Store
import pushswap.utils.InputParser

/*
        Use in my code:
                * require and check, maybe assert

 */


fun main(args: Array<String>) {
        val store: Store

        if (args.size == 1) {
                store = InputParser.startFromQuotes(args[0])
        } else {
                InputParser.checker(args)
                store = InputParser.createStoreAndStack(args)
        }
        println(store.stackA)
}
