package errors.lexer

import lexer.CharacterLocation

class UnknownCharException(char: Char, characterLocation: CharacterLocation, line: String):
    Exception("The lexer encountered the unknown character '$char' in line '${characterLocation.lineIndex}' at index ${characterLocation.charIndex}!:\n${characterLocation.lineIndex} - $line")