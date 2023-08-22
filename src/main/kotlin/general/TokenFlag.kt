package general

enum class TokenFlag {
    Useless,

    Exit,

    Literal,

    Number,

    OpenBracket,
    ClosedBracket,

    Semicolon,;

    override fun toString(): String {
        return this.name;
    }
}

