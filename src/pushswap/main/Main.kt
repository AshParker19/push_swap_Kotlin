package pushswap.main

import pushswap.models.*
import pushswap.constants.Constants
import pushswap.utils.InputParser

/*
        Use in my code:
                * require and check, maybe assert

 */


fun main(args: Array<String>) {
        val store = Store()

        if (args.size == 1) {
                InputParser.startFromQuotes(args[0], store)
        } else {
                InputParser.checker(args)
                InputParser.createStack(args, store)
        }
}
