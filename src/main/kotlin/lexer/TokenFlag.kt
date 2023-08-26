package lexer

enum class TokenFlag {
    Useless,

    Exit,
    Let,
    Print,

    StringLiteral,
    IdentifierLiteral,
    NumberLiteral,

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
            IdentifierLiteral -> "%literal%"
            NumberLiteral -> "%number%"
            Assign -> "="
            Plus -> "+"
            Minus -> "-"
            Mul -> "*"
            Div -> "/"
            OpenBracket -> "("
            ClosedBracket -> ")"
            Semicolon -> ";"
            StringLiteral -> "string"
            Print -> "print"
        }
    }
}

