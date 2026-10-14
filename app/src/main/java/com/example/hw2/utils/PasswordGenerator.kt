package com.example.hw2.utils

fun generatePassword(): String {
    val passwordCharacters = mutableListOf<Char>()

    passwordCharacters.add(Consts.LATIN_LETTERS.random())
    passwordCharacters.add(Consts.DIGITS.random())

    for (i in 1 ..Consts.PASSWORD_LENGTH - 2) {
        passwordCharacters.add(Consts.PASSWORD_CHARACTERS.random())
    }

    passwordCharacters.shuffle()

    return passwordCharacters.joinToString("")

}