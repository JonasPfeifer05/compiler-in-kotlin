package errors.lexer

import lexer.TokenLocation

class NonTerminatedCharException(tokenLocation: TokenLocation, line: String):
    Exception("The lexer found a char literal in line ${tokenLocation.lineIndex} from index ${tokenLocation.sectionIndex.first} to ${tokenLocation.sectionIndex.last} but it wasn't terminated with a \"'\"!\n${tokenLocation.lineIndex} | $line")