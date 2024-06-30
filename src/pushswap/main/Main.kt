package src.pushswap.main

/*
        Use in my code:
                * require and check, maybe assert

 */

data class Person(val name: String, val age: Int)

fun main() {
        val person1 = Person("Alice", 30)

//        println("Name: ${person1.name}, Age: ${person1.age}")
        val name = person1.component1()
        val age = person1.component2()
        println("Name $name, Age $age") // Uses auto-generated toString()
}
