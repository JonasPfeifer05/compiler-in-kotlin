package lexer

import general.LocationToken

class UnknownCharException(char: Char, locationToken: LocationToken, line: String):
    Exception("The lexer encountered the unknown character '$char' in line '${locationToken.lineIndex}' at index ${locationToken.sectionIndex.first}!:\n${locationToken.lineIndex} - $line");