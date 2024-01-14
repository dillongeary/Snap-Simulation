import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

fun compareRanks(newCard: Card, oldCard: Card) : Boolean {
    return (newCard.rank == oldCard.rank)
}

interface CardPileListener {
    suspend fun onCardUpdate(topCards: MutableList<Card?>)
}

class Player(
    val name: Int,
    var myDeck: ArrayList<Card>,
    val cardPile: CardPile,
    val compareFunction: (Card, Card) -> Boolean = { a, b -> compareRanks(a,b)}
) : CardPileListener {
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

    override suspend fun onCardUpdate(topCards: MutableList<Card?>) {
        //println("player $name checking ${newCard.name} against ${oldCard?.name}")
        val delay = Random.nextInt(100,400)
        delay(delay.milliseconds)

        checkForMatchingCards(topCards)?.let {(cardA,cardB) ->
            println("Player $name : Snap!")
            cardPile.snap(name,cardA,cardB)
        }
    }

    fun getNextCard() : Card {
        return myDeck.removeFirst()
    }

}