package lexer

data class Token(val value: String, val flag: TokenFlag, val tokenLocation: TokenLocation)