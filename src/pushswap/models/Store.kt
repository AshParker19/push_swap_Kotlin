package pushswap.models

class Store( // primary constructor

    // data needed for the algorithm
    var countA: Int = 0,
    var countB: Int = 0,
    var chunkSize: Int = 0,
    var chunkNum: Int = 0,
    var biggest: Int = Int.MIN_VALUE,
    var sndBiggest: Int = Int.MIN_VALUE,
    var smallest: Int = Int.MAX_VALUE,

    // lists which represent stacks A and B
    var stackA: MutableList<StackElement> = mutableListOf(),
    var stackB: MutableList<StackElement> = mutableListOf()
) {
    fun pa(element: StackElement) {
        stackA.add(element)
        countA++
    }
}