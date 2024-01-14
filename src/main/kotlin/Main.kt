import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.restrictTo
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds



class Main : CliktCommand() {
    val numberOfPlayers: Int by option("-p","--players").int().default(2).help("Number of Players")
    val gameMode: GameMode by option("-m","--mode").choice("ranks" to GameMode.Ranks, "suits" to GameMode.Suites, "strict" to GameMode.Both).default(GameMode.Ranks).help("The Rules for a Snap Condition")
    val numberOfDecks: Int by option("-d","--decks").int().restrictTo(1).default(1).help("The Number of Decks in Play")

    override fun run() {

        if (gameMode == GameMode.Both && numberOfDecks == 1) {
            echo("Impossible to Snap! with only 1 deck on Strict Game Mode.\nExiting Game...")
            return
        }

        val cardPile = CardPile(numberOfPlayers)
        val playerList : MutableList<Player> = mutableListOf()

        val scoreBoard : MutableList<Int> = mutableListOf()
        repeat(numberOfPlayers) {scoreBoard.add(0)}

        //Prints out how many points the winning player recieves, and prints out the score board
        fun updateScore(player:Int, amount: Int) {
            echo("$amount ${if (amount == 1) {"card"} else {"cards"}} to Player ${player+1}")
            scoreBoard[player] += amount
            echo("\nScore Board")
            for (score in scoreBoard.withIndex()) {
                echo("Player ${score.index+1} : ${score.value} ${if (score.value == 1) {"card"} else {"cards"}}")
            }
        }

        //Create an array list of cards by performing cartesian product between all suites and card ranks, and returning each pair as a card object.
        val singleDeck = ArrayList(
            Suite.entries.flatMap { suite ->
                Rank.entries.map { rank ->
                    Card(suite, rank)
                }
            }
        )

        var decks = singleDeck

        repeat(numberOfDecks - 1) {
            decks.addAll(singleDeck)
        }

        //Randomly shuffle the deck
        decks.shuffle()

        /*
        split the deck into N lists, where N is the number of players and each deck contains cards separated one at a time.
        e.g. a list of [1,2,3,4,5,6] with N = 2 would equal [[1,3,5],[2,4,6]]
         */
        val splitUpDeck : List<List<Card>> = decks
            .withIndex()
            .groupBy { it.index % numberOfPlayers }
            .map { it.value.map { it.value } }


        /*
        initiate N player objects, where N is the number of players.
        Each player gets part of the deck, and access to the card pile class.
        Each player is also added to the list of players in main, and the list of observers in CardPile
         */
        repeat(numberOfPlayers) {
            val newPlayer = Player(
                it,
                ArrayList(splitUpDeck.get(it)),
                cardPile,
                compareFunction = gameMode.predicate
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
        var ifPlayersHaveCards = true
        while (ifPlayersHaveCards) {
            ifPlayersHaveCards = false
            for (player in playerList) {
                if (player.myDeck.size > 0) {
                    ifPlayersHaveCards = true
                    runBlocking {
                        val nextCard = player.getNextCard()
                        cardPile.updateCardPiles(player.name, nextCard)
                        echo("Player ${player.name+1} plays ${nextCard.name}\n${cardPile.topCards.map { if (it == null) {"[     ]"} else {it.icon}}.joinToString(" ")}\n")
                        cardPile.updateObservers()
                        delay(700.milliseconds)
                        val result = cardPile.getAndResetQuickestPlayer()
                        result?.let {
                            (quickestPlayer, score) -> updateScore(quickestPlayer, score)
                            delay(3000.milliseconds)
                            echo()
                        }
                    }
                }
            }
        }



    }
}

fun main(args: Array<String>) {
    Main().main(args)
}