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

    And,

    OpenParent,
    ClosedParent,
    OpenBracket,
    ClosedBracket,

    Comma,
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
            And -> "&"
            OpenParent -> "("
            ClosedParent -> ")"
            Semicolon -> ";"
            Comma -> ","
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

