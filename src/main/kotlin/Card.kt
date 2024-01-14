enum class Suite (val print: String){
    Hearts("Hrt"),
    Spades("Spd"),
    Diamonds("Dmd"),
    Clubs("Clb")
}

enum class Rank(val print: String) {
    Ace("A"),
    Two("2"),
    Three("3"),
    Four("4"),
    Five("5"),
    Six("6"),
    Seven("7"),
    Eight("8"),
    Nine("9"),
    Ten("T"),
    Jack("J"),
    Queen("Q"),
    King("K")
}

class Card (
    val suite: Suite,
    val rank: Rank
) {
    val name = "$rank of $suite"
    val icon = "[ ${rank.print} ${suite.print} ]"
}