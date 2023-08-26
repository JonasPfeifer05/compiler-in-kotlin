package errors.lexer

import lexer.TokenLocation

class NonTerminatedStringException(tokenLocation: TokenLocation, line: String):
    Exception("The lexer found a string literal in line ${tokenLocation.lineIndex} from index ${tokenLocation.sectionIndex.first} to ${tokenLocation.sectionIndex.last} but it wasn't terminated with a '\"'!\n${tokenLocation.lineIndex} | $line")