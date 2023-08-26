package errors.parser

import general.LineBuffer
import lexer.TokenFlag

class RanOutOfTokensButExpectedTokenException(expectedTokenFlags: Array<out TokenFlag>, lineIndex: Int, lineBuffer: LineBuffer):
    Exception("The parser expected a token in line $lineIndex with one of these types: ${expectedTokenFlags.contentToString()} but ran out of tokens!\n$lineIndex | ${lineBuffer.getLineOptional(lineIndex).get()}")