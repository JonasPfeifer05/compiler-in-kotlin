package parser

import general.LocationToken
import general.Token

class UnexpectedTokenException(token: Token):
    Exception("The parser encountered an unexpected token with the value '${token.value}' in line ${token.locationToken.lineIndex} from index ${token.locationToken.sectionIndex.first} to ${token.locationToken.sectionIndex.last}") {
}