# Snap Simulation

A simulation of a game of snap ran through the command line, written in Kotlin

## Description

This project is a simulation of a game of snap between two (or more) players, where the placing of cards and the calling of snap is all simulated using random delays to introduce randomness and "realism" into the system.

Upon reaching a snap, the first player to call out "Snap!" wins the amount of cards in the pile of cards underneath the two matching cards. The game ends when all cards are placed, 

## To Run
To run a basic game of snap, with the default parameters, clone the repo and run the following command in the base directory:
```bash
java -jar Snap-Simulation-run.jar
``` 

### Optional Flags

There are optional flags to alter the rules of the game, that can be entered when running the simulation.

`-p, --players` takes in an integer representing the number of players. Must be larger than 1, default 2.
<br>`-m, --mode` take in the options `ranks`,`suits` or `strict` reprenting the rules for a snap, where ranks is matching on the rank of the card, suits is matching on the suit, and strict is matching on both. Note, more than 1 deck is required to play Strict. Default is `ranks`
<br>`-d, --decks` takes in an integer representing the number of decks in play. Must be larger than 0, default 1.

For example, you can type in:
```bash
java -jar Snap-Simulation-run.jar -p 3 -m strict -d 2
```
for a game with 3 players, on strict rules with 2 decks of cards.

`-h, --help` shows this information in the command line.

## Extra Info

Project completed as apart of an interview test in a graduate developer job interview.

Optional Extras:
- [x] Multiple Decks of Cards
- [x] Multiple Players
- [ ] Multiple Rounds
- [ ] Multiple Stop Conditions
- [x] Multiple Types of Snap
