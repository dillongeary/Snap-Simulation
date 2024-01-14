import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CardPile (numberOfPlayers:Int) {

    //Lists of length N to store both the top card in play, and the amount of cards in each players stack, where N is the amount of players
    val topCards : MutableList<Card?> = mutableListOf()
    val amountOfCards : MutableList<Int> = mutableListOf()

    //initialisation function to fill the above lists will null / 0
    init {
        repeat(numberOfPlayers) {
            topCards.add(null)
            amountOfCards.add(0)
        }
    }

    //A list of listeners that will be updated with changes to cardPiles
    var listeners: MutableList<CardPileListener> = mutableListOf()

    /*
    A tracker for whatever player says snap quickest, and the score they get.
    A Mutex is used so that only one player can alter this at once
     */
    val quickestPlayerMutex = Mutex()
    var quickestPlayer : Int? = null
    var score : Int? = null

    /*
    A function that the players will call, that sets the quickest player to themselves, and calculates their score increase.
    Only one player can be the quickest, so quickestPlayer will only be altered if it is null
     */
    suspend fun snap(player: Int, pileA: Int, pileB: Int) {
        quickestPlayerMutex.withLock {
            if (quickestPlayer == null) {
                quickestPlayer = player
                score = amountOfCards[pileA] + amountOfCards[pileB]
                amountOfCards[pileA] = 0
                amountOfCards[pileB] = 0
                topCards[pileA] = null
                topCards[pileB] = null
            }
        }
    }

    //A function that returns the quickest player and their score increase, and resets both to null
    fun getAndResetQuickestPlayer() : Pair<Int,Int>? {
        val qP = quickestPlayer
        val sC = score
        quickestPlayer = null
        score = null
        qP?.let { q -> sC?.let { s -> return Pair(q,s) } }
        return null
    }

    //Adds a new listener to the list of listeners
    fun addPlayer(player:CardPileListener) {
        listeners.add(player)
    }

    //Updates the card piles with a new card
    fun updateCardPiles(position: Int, newCard: Card) {
        topCards[position] = newCard
        amountOfCards[position] += 1
    }

    /*
    Calls to all the listeners that the card piles has updated.
    Uses coroutines to call to all simultaneously
    */
    suspend fun updateObservers() {
        coroutineScope {
            listeners.forEach {
                launch { it.onCardUpdate(topCards) }
            }
        }
    }
}