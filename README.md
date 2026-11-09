# Overview

As a software engineer, I wanted to learn Kotlin, a modern language used for
Android and server development, by building a small but complete program
with it. This project helped me practice Kotlin's syntax and features such as
immutable values, data classes, collections and the `when` keyword.

The Vocabulary Flashcard Quiz is a command-line program that helps the user
study English–Spanish vocabulary. From a menu, the user can add new
flashcards, list the whole deck, take a 5-question quiz (each question
randomly asks English → Spanish or Spanish → English), get a score with a
message, and review the words they have missed most often. The program
starts with a small set of words so the quiz works right away.

To run it, open the project in IntelliJ IDEA and run `main()` in
`src/Main.kt`, or compile and run it from a terminal:

```
kotlinc src/Main.kt -include-runtime -d FlashcardQuiz.jar
java -jar FlashcardQuiz.jar
```

My purpose for writing this software was to learn the fundamentals of Kotlin
and to build a base that I can later turn into an Android app.

[Software Demo Video](https://www.youtube.com/watch?v=2OOo0wdruq4)

# Development Environment

- IntelliJ IDEA Community Edition
- Java Development Kit (OpenJDK 21)
- Kotlin 2.1 (Kotlin standard library only)
- Git and GitHub

# Useful Websites

- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Kotlin Basic Syntax](https://kotlinlang.org/docs/basic-syntax.html)
- [Kotlin Data Classes](https://kotlinlang.org/docs/data-classes.html)
- [Kotlin Collections Overview](https://kotlinlang.org/docs/collections-overview.html)
- [Kotlin Playground](https://play.kotlinlang.org/)

# Future Work

- Save the flashcards to a file so new cards are kept between sessions
- Let the user choose how many questions are in a quiz
- Turn the program into an Android app with a screen for each feature
