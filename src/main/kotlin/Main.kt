import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds

class Main : CliktCommand() {
    val players: Int by option().int().default(2).help("Number of Players")



    override fun run() {

        val cardPile = CardPile(players)
        val playerList : MutableList<Player> = mutableListOf()

        val scoreBoard : MutableList<Int> = mutableListOf()
        repeat(players) {scoreBoard.add(0)}

        fun updateScore(player:Int, amount: Int) {
            echo("Player $player Called Snap First!")
            scoreBoard[player] += amount
            echo(scoreBoard)
        }

        //Create an array list of cards by performing cartesian product between all suites and card ranks, and returning each pair as a card object.
        val deck = ArrayList(
            Suite.entries.flatMap { suite ->
                Rank.entries.map { rank ->
                    Card(suite, rank)
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


        /*
        initiate N player objects, where N is the number of players.
        Each player gets part of the deck, and access to the card pile class.
        Each player is also added to the list of players in main, and the list of observers in CardPile
         */
        repeat(players) {
            val newPlayer = Player(
                it,
                ArrayList(splitUpDeck.get(it)),
                cardPile
            )
            playerList.add(newPlayer)
            cardPile.addPlayer(newPlayer)
        }

        /*
        Main simulation loop.
        For each player, get their next card and add it to the card pile.
        Wait 1 second, then check to see if anyone has called snap via the getAndResetQuickestPlayer.
        Update and print the score if changed.
         */
        while (true) {
            for (player in playerList) {
                runBlocking {
                    val nextCard = player.getNextCard()
                    echo("Player ${player.name} : ${nextCard.name}")
                    cardPile.updateCard(player.name,nextCard)
                    delay(1000.milliseconds)
                    val result = cardPile.getAndResetQuickestPlayer()
                    result?.let { (quickestPlayer, score) -> updateScore(quickestPlayer,score) }
                }
            }
        }


    }
}

fun main(args: Array<String>) {
    Main().main(args)
}