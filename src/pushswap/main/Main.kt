package pushswap.main

import pushswap.utils.InputParser

/*
        Use in my code:
                * require and check, maybe assert

 */


fun main(args: Array<String>) {

        if (args.size == 1) {
                InputParser.startFromQuotes(args[0])
        } else {
                InputParser.checker(args)
                InputParser.createStack(args)
        }
}
