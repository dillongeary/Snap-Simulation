import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import Suite
import Letter

class Main : CliktCommand() {
    val players: Int by option().int().default(2).help("Number of Players")

    override fun run() {

        //Create an array list of cards by performing cartesian product between all suites and card values, and returning each pair as a card object.
        var deck = ArrayList(
            Suite.entries.flatMap { suite ->
                Letter.entries.map { number ->
                    Card(suite, number)
                }
            }
        )

        //Randomly shuffle the deck
        deck.shuffle()

        /*
        split the deck into N lists, where N is the number of players and each deck contains cards separated one at a time.
        e.g. a list of [1,2,3,4,5,6] with N = 2 would equal [[1,3,5],[2,4,6]]
         */
        val splitUpDeck : List<List<Card>> = deck
            .withIndex()
            .groupBy { it.index % players }
            .map { it.value.map { it.value } }
    }
}

fun main(args: Array<String>) {
    Main().main(args)
}