package pushswap.algorithm

import pushswap.models.Store

object Algorithm {

    fun sortMain(store: Store) {
        val copy = IntArray(store.stackA.size)

        when (store.stackA.size) {
            2 -> store.sa()
            3 -> sort3(store)
        }
    }

    private fun sort3(store: Store) {

    }
}