package lexer

enum class TokenFlag {
    Useless,

    Exit,
    Let,

    Literal,
    Number,

    Assign,

    OpenBracket,
    ClosedBracket,

    Semicolon,;

    override fun toString(): String {
        return this.name
    }
}

