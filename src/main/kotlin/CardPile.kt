import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CardPile (val numberOfPlayers:Int) {

    val topCards : MutableList<Card?> = mutableListOf()
    val amountOfCards : MutableList<Int> = mutableListOf()

    init {
        repeat(numberOfPlayers) {
            topCards.add(null)
            amountOfCards.add(0)
        }
    }

    var observers: MutableList<CardPileListener> = mutableListOf()

    val quickestPlayerMutex = Mutex()
    var quickestPlayer : Int? = null
    var score : Int? = null

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

    fun getAndResetQuickestPlayer() : Pair<Int,Int>? {
        val qP = quickestPlayer
        val sC = score
        quickestPlayer = null
        score = null
        qP?.let { q -> sC?.let { s -> return Pair(q,s) } }
        return null
    }

    fun addPlayer(player:CardPileListener) {
        observers.add(player)
    }

    fun updateCard(player: Int, newCard: Card) {
        //println("updating card : ${topCards}")
        topCards[player] = newCard
        amountOfCards[player] += 1
    }

    suspend fun updateObservers() {
        coroutineScope {
            observers.forEach {
                launch { it.onCardUpdate(topCards) }
            }
        }
    }
}