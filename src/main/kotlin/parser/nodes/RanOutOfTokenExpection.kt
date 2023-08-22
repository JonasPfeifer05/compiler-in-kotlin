package parser.nodes

class RanOutOfTokenException: Exception("The parser expected a token but ran out of token!") {
}