package lexer

enum class TokenFlag {
    Useless,

    Exit,
    Let,
    Print,

    StringLiteral,
    IdentifierLiteral,
    NumberLiteral,

    U64Type,
    StringType,

    Assign,

    Plus,
    Minus,
    Mul,
    Div,

    OpenParent,
    ClosedParent,
    OpenBracket,
    ClosedBracket,

    Colon,
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
            OpenParent -> "("
            ClosedParent -> ")"
            Semicolon -> ";"
            StringLiteral -> "string"
            Print -> "print"
            U64Type -> "u64"
            StringType -> "string"
            Colon -> ":"
            OpenBracket -> "["
            ClosedBracket -> "]"
        }
    }
}

