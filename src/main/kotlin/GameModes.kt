
enum class GameMode (val predicate: (Card, Card) -> Boolean) {
    Ranks  ( { new, old -> compareRanks(new,old)  } ),
    Suites ( { new, old -> compareSuites(new,old) } ),
    Both   ( { new, old -> compareCard(new,old)   } )
}

fun compareRanks(newCard: Card, oldCard: Card) : Boolean {
    return (newCard.rank == oldCard.rank)
}

fun compareSuites(newCard: Card, oldCard: Card) : Boolean {
    return (newCard.suite == oldCard.suite)
}

fun compareCard(newCard: Card, oldCard: Card) : Boolean {
    return (newCard.rank == oldCard.rank && newCard.suite == oldCard.suite)
}