enum class GameMode (val predicate: (Card, Card) -> Boolean) {
    Ranks  ( { new, old -> compareRanks(new,old)  } ),
    Suites ( { new, old -> compareSuites(new,old) } ),
    Both   ( { new, old -> compareCard(new,old)   } )
}

//a predicate function that checks if two cards ranks are the same
fun compareRanks(newCard: Card, oldCard: Card) : Boolean {
    return (newCard.rank == oldCard.rank)
}

//a predicate function that checks if two cards suites are the same
fun compareSuites(newCard: Card, oldCard: Card) : Boolean {
    return (newCard.suite == oldCard.suite)
}

//a predicate function that checks if two cards suites and ranks are the same
fun compareCard(newCard: Card, oldCard: Card) : Boolean {
    return (newCard.rank == oldCard.rank && newCard.suite == oldCard.suite)
}