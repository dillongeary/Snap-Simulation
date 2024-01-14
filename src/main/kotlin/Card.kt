enum class Suite {
    Heart, Spade, Diamond, Club
}

enum class Letter {
    Ace,Two,Three,Four,Five,Six,Seven,Eight,Nine,Ten,Jack,Queen,King
}

class Card (val suite: Suite, val number: Letter) {
    val name = "$number of $suite"
}