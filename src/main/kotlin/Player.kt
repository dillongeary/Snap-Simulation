import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

//An interface for classes listening to the card pile in the CardPile class
interface CardPileListener {
    suspend fun onCardUpdate(topCards: MutableList<Card?>)
}

/*
A class representing a player in the game of snap.
Handles its current card, and logic for comparing the cards on the table.
 */
class Player(
    val name: Int,
    var myDeck: ArrayList<Card>,
    val cardPile: CardPile,
    val compareFunction: (Card, Card) -> Boolean = { a, b -> compareRanks(a,b)}
) : CardPileListener {
    /*
    A funciton for comparing all the cards on the table for any matches.
    Returns the indexes for the piles that do contains matches if one is found, null if not
     */
    fun checkForMatchingCards(topCards: MutableList<Card?>): Pair<Int, Int>? {
        for (i in 0..<topCards.size) {
            for (j in i+1..<topCards.size) {
                topCards[i]?.let { cardA ->
                    topCards[j]?.let { cardB ->
                        if (compareFunction(cardA,cardB)) {
                            return Pair(i,j)
                        }
                    }
                }
            }
        }
        return null
    }

    /*
    A function that is called when recieving an update from the Card Pile Listener
    Simply waits a random amount of time based off of average human reaction time,
    checks for matches and calls the snap function if one is found
     */
    override suspend fun onCardUpdate(topCards: MutableList<Card?>) {
        val delay = Random.nextInt(100,400)
        delay(delay.milliseconds)

        checkForMatchingCards(topCards)?.let {(cardA,cardB) ->
            println("Player ${name+1} : Snap!")
            cardPile.snap(name,cardA,cardB)
        }
    }

    //returns the next card in the players hand, and removes it from their hand
    fun getNextCard() : Card {
        return myDeck.removeFirst()
    }

}