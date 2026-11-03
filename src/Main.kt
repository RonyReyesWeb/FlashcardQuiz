/*
 * Vocabulary Flashcard Quiz
 *
 * A command-line program that helps the user study English–Spanish vocabulary.
 * The user can add flashcards, list the deck, take a quiz with a score, and
 * review the words they missed most often.
 *
 * Kotlin features demonstrated:
 *  - mutable (var) and immutable (val) variables
 *  - expressions, conditionals (if/else) and loops (while, for)
 *  - functions with parameters and return values
 *  - a class (Deck) and a data class (Flashcard)
 *  - collections (MutableList, filter, sortedByDescending)
 *  - the when keyword (menu, score messages, quiz direction)
 */

// Number of questions in one quiz. It never changes, so it is a val.
const val QUIZ_LENGTH = 5

/**
 * One flashcard. A data class automatically gives us toString(), equals()
 * and copy(). timesMissed is a var because it changes every time the user
 * gets the card wrong.
 */
data class Flashcard(val english: String, val spanish: String, var timesMissed: Int = 0)

/**
 * The direction of a quiz question: show the English word and ask for the
 * Spanish one, or the other way around.
 */
enum class Direction { ENGLISH_TO_SPANISH, SPANISH_TO_ENGLISH }

/**
 * A deck of flashcards stored in a MutableList, with methods to add,
 * list and choose cards.
 */
class Deck {
    private val cards = mutableListOf<Flashcard>()

    /** Number of cards currently in the deck. */
    val size: Int
        get() = cards.size

    /** Add a card unless the same English word is already in the deck. Returns true if added. */
    fun add(card: Flashcard): Boolean {
        val exists = cards.any { it.english.equals(card.english, ignoreCase = true) }
        if (exists) return false
        cards.add(card)
        return true
    }

    /** Return a read-only copy of all cards, sorted alphabetically by English word. */
    fun allCards(): List<Flashcard> = cards.sortedBy { it.english.lowercase() }

    /** Pick up to [count] random cards for a quiz. */
    fun pickForQuiz(count: Int): List<Flashcard> = cards.shuffled().take(count)

    /** Return only the cards the user has missed at least once, most-missed first. */
    fun missedCards(): List<Flashcard> =
        cards.filter { it.timesMissed > 0 }.sortedByDescending { it.timesMissed }
}

/** Create a deck with a few starter words so the quiz works right away. */
fun createStarterDeck(): Deck {
    val deck = Deck()
    val starterWords = listOf(
        "house" to "casa", "book" to "libro", "water" to "agua",
        "friend" to "amigo", "work" to "trabajo", "computer" to "computadora",
        "family" to "familia", "city" to "ciudad"
    )
    for ((english, spanish) in starterWords) {
        deck.add(Flashcard(english, spanish))
    }
    return deck
}

/** Read one line from the user. Returns an empty string if there is no more input. */
fun readInput(prompt: String): String {
    print(prompt)
    return readlnOrNull()?.trim() ?: ""
}

/**
 * Compare the user's answer with the correct one, ignoring upper/lower case
 * and extra spaces. Returns true if they match.
 */
fun isCorrect(answer: String, expected: String): Boolean =
    answer.trim().equals(expected.trim(), ignoreCase = true)

/** Calculate the score as a whole-number percentage. Returns 0 if there were no questions. */
fun scorePercent(correct: Int, total: Int): Int =
    if (total == 0) 0 else correct * 100 / total

/** Choose a message for the final score using when with ranges. */
fun scoreMessage(percent: Int): String = when (percent) {
    100 -> "Perfect score! ¡Excelente!"
    in 80..99 -> "Great job! You know most of these words."
    in 50..79 -> "Good effort. Review the words you missed."
    else -> "Keep practicing. Every session helps!"
}

/** Ask the user for a new English–Spanish pair and add it to the deck. */
fun addCard(deck: Deck) {
    val english = readInput("English word: ")
    val spanish = readInput("Spanish word: ")
    if (english.isEmpty() || spanish.isEmpty()) {
        println("Both words are required. Card not added.")
        return
    }
    val added = deck.add(Flashcard(english, spanish))
    println(if (added) "Added: $english = $spanish" else "\"$english\" is already in the deck.")
}

/** Print every card in the deck in a simple table. */
fun listCards(deck: Deck) {
    println("\n%-15s %-15s %s".format("English", "Spanish", "Missed"))
    println("-".repeat(38))
    for (card in deck.allCards()) {
        println("%-15s %-15s %d".format(card.english, card.spanish, card.timesMissed))
    }
    println("Total cards: ${deck.size}")
}

/**
 * Run one quiz of up to QUIZ_LENGTH questions. Each question randomly shows
 * the English or the Spanish word. Updates timesMissed for wrong answers.
 */
fun runQuiz(deck: Deck) {
    val questions = deck.pickForQuiz(QUIZ_LENGTH)
    if (questions.isEmpty()) {
        println("Add some cards before taking a quiz.")
        return
    }
    var correct = 0
    for ((index, card) in questions.withIndex()) {
        val direction = Direction.entries.random()
        // when as an expression returns a Pair: (word to show, expected answer)
        val (shown, expected) = when (direction) {
            Direction.ENGLISH_TO_SPANISH -> card.english to card.spanish
            Direction.SPANISH_TO_ENGLISH -> card.spanish to card.english
        }
        val answer = readInput("\nQuestion ${index + 1}/${questions.size}: Translate \"$shown\": ")
        if (isCorrect(answer, expected)) {
            correct++
            println("Correct!")
        } else {
            card.timesMissed++
            println("Not quite. The answer is \"$expected\".")
        }
    }
    val percent = scorePercent(correct, questions.size)
    println("\nYou got $correct out of ${questions.size} ($percent%).")
    println(scoreMessage(percent))
}

/** Show the words the user has missed, so they know what to review. */
fun showMissed(deck: Deck) {
    val missed = deck.missedCards()
    if (missed.isEmpty()) {
        println("No missed words yet. Take a quiz first!")
        return
    }
    println("\nWords to review:")
    for (card in missed) {
        val times = if (card.timesMissed == 1) "time" else "times"
        println("  ${card.english} = ${card.spanish} (missed ${card.timesMissed} $times)")
    }
}

/** Show the menu in a loop until the user chooses to exit. */
fun main() {
    val deck = createStarterDeck()
    var running = true
    println("=== Vocabulary Flashcard Quiz ===")
    while (running) {
        println("\n1. Add a flashcard")
        println("2. List all flashcards")
        println("3. Take a quiz")
        println("4. Review missed words")
        println("5. Exit")
        when (readInput("Choose an option: ")) {
            "1" -> addCard(deck)
            "2" -> listCards(deck)
            "3" -> runQuiz(deck)
            "4" -> showMissed(deck)
            "5", "" -> {
                println("¡Hasta luego!")
                running = false
            }
            else -> println("Please choose 1-5.")
        }
    }
}
