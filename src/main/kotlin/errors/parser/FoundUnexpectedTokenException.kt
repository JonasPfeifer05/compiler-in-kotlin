package errors.parser

import general.LineBuffer
import lexer.Token
import lexer.TokenFlag

class FoundUnexpectedTokenException(got: Token, expected: Array<out TokenFlag>, lineBuffer: LineBuffer):
    Exception("The parser expected an token with one of the types from these: ${expected.contentToString()} but got a token of type ${got.flag.name} in line ${got.tokenLocation.lineIndex} from index ${got.tokenLocation.sectionIndex.first} to ${got.tokenLocation.sectionIndex.last}\n${got.tokenLocation.lineIndex} | ${lineBuffer.getLineOptional(got.tokenLocation.lineIndex).get()}")