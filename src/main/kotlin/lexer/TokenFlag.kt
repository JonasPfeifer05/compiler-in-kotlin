package lexer

enum class TokenFlag {
    Useless,

    Exit,
    Let,

    Literal,
    Number,

    Assign,

    Plus,
    Minus,
    Mul,
    Div,

    OpenBracket,
    ClosedBracket,

    Semicolon,;

    override fun toString(): String {
        return this.name
    }

    fun getSymbol(): String {
        return when (this) {
            Useless -> ""
            Exit -> "exit"
            Let -> "let"
            Literal -> "%literal%"
            Number -> "%number%"
            Assign -> "="
            Plus -> "+"
            Minus -> "-"
            Mul -> "*"
            Div -> "/"
            OpenBracket -> "("
            ClosedBracket -> ")"
            Semicolon -> ";"
        }
    }
}

