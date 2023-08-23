package parser

import lexer.Token

class UnexpectedTokenException(token: Token):
    Exception("The parser encountered an unexpected token with the value '${token.value}' in line ${token.tokenLocation.lineIndex} from index ${token.tokenLocation.sectionIndex.first} to ${token.tokenLocation.sectionIndex.last}") {
}