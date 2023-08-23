package parser

import lexer.Token
import lexer.TokenFlag

class ExpectedTokenButGotException(got: Token, expected: Array<out TokenFlag>):
    Exception("The parser expected an token of one type from these ${expected.contentToString()} but got a token of type ${got.flag.name} in line ${got.tokenLocation.lineIndex} from index ${got.tokenLocation.sectionIndex.first} to ${got.tokenLocation.sectionIndex.last}") {
}