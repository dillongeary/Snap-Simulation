enum class Suite {
    Hearts, Spades, Diamonds, Clubs
}

enum class Rank {
    Ace,Two,Three,Four,Five,Six,Seven,Eight,Nine,Ten,Jack,Queen,King
}

class Card (
    val suite: Suite,
    val rank: Rank
) {
    val name = "$rank of $suite"
}