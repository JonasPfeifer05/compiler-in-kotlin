package lexer

import errors.lexer.NonTerminatedStringException
import errors.lexer.UnknownCharException
import general.LineBuffer
import java.util.Optional
import kotlin.jvm.optionals.getOrDefault

val LITERAL_BEGIN_RANGE = 'A'..'z'
val LITERAL_BODY_RANGE = 'A'..'z'
val NUMBER_RANGE = '0'..'9'

val EMPTY_CHARACTERS = charArrayOf(' ', '\t')

class Lexer(private val lineBuffer: LineBuffer) {
    private var lineIndex: UInt = 0u
    private var charIndex: UInt = 0u
    private var currentLine: String

    init {
        currentLine = this.lineBuffer.getLineOptional(this.lineIndex).getOrDefault("")
    }

    fun getTokens(): List<Token> {
        val tokens: MutableList<Token> = mutableListOf()

        var token = this.getNextToken()
        while (token.isPresent) {
            tokens.add(token.get())
            token = this.getNextToken()
        }

        return tokens.filter { it.flag != TokenFlag.Useless }
    }

    private fun getNextToken(): Optional<Token> {
        if (this.peekChar().isEmpty) {
            if (gotMoreLines()) {
                this.advanceToNewLine()
                return Optional.of(Token("\\n", TokenFlag.Useless, TokenLocation(this.lineIndex, this.charIndex..this.charIndex)))
            }

            return Optional.empty()
        }
        val currentChar = this.consumeChar()

        if (EMPTY_CHARACTERS.contains(currentChar))
            return Optional.of(Token("empty", TokenFlag.Useless, TokenLocation(this.lineIndex, this.charIndex..this.charIndex)))

        val tokenStartIndex = this.previousCharIndex()
        val (value, flag) = when (currentChar) {
            ';' -> Pair(";", TokenFlag.Semicolon)
            ':' -> Pair(";", TokenFlag.Colon)
            '(' -> Pair("(", TokenFlag.OpenParent)
            ')' -> Pair(")", TokenFlag.ClosedParent)
            '[' -> Pair(")", TokenFlag.OpenBracket)
            ']' -> Pair(")", TokenFlag.ClosedBracket)
            '=' -> Pair("=", TokenFlag.Assign)
            '+' -> Pair("+", TokenFlag.Plus)
            '-' -> Pair("-", TokenFlag.Minus)
            '*' -> Pair("*", TokenFlag.Mul)
            '/' -> Pair("/", TokenFlag.Div)
            in LITERAL_BEGIN_RANGE -> {
                val value = currentChar + this.readMatchingSequence { this in LITERAL_BODY_RANGE || this in NUMBER_RANGE }
                when (value) {
                    "exit" -> Pair(value, TokenFlag.Exit)
                    "let" -> Pair(value, TokenFlag.Let)
                    "print" -> Pair(value, TokenFlag.Print)
                    "u64" -> Pair(value, TokenFlag.U64Type)
                    "string" -> Pair(value, TokenFlag.StringType)
                    else -> Pair(value, TokenFlag.IdentifierLiteral)
                }
            }

            in NUMBER_RANGE -> Pair(
                currentChar + this.readMatchingSequence { this in NUMBER_RANGE },
                TokenFlag.NumberLiteral,
            )

            '"' -> {
                Pair(this.readString(tokenStartIndex), TokenFlag.StringLiteral)
            }

            else ->
                throw UnknownCharException(
                    currentChar,
                    CharacterLocation(this.lineIndex, tokenStartIndex),
                    this.currentLine,
                )
        }

        return Optional.of(
            Token(value, flag, TokenLocation(this.lineIndex, tokenStartIndex..this.previousCharIndex()))
        )
    }

    private fun readString(tokenStartIndex: UInt): String {
        val builder = StringBuilder()

        var peekChar = this.peekChar()
        while (peekChar.isPresent && peekChar.get() != '"') {
            builder.append(
                this.consumeChar()
            )

            peekChar = this.peekChar()
        }
        if (peekChar.isEmpty)
            throw NonTerminatedStringException(TokenLocation(this.lineIndex, tokenStartIndex..this.charIndex), this.lineBuffer.getLineOptional(this.lineIndex).get())

        this.consumeChar()

        return builder.toString()
    }

    private fun readMatchingSequence(doesMatch: Char.() -> Boolean): String {
        val builder = StringBuilder()

        var peekChar = this.peekChar()
        while (peekChar.isPresent && peekChar.get().doesMatch()) {
            builder.append(
                this.consumeChar()
            )

            peekChar = this.peekChar()
        }

        return builder.toString()
    }

    private fun consumeChar(): Char {
        val readChar = this.currentLine.getOptional(this.charIndex)
        this.charIndex++

        return readChar.get()
    }

    private fun peekChar(): Optional<Char> {
        return this.currentLine.getOptional(this.charIndex)
    }

    private fun advanceToNewLine() {
        this.charIndex = 0u
        this.lineIndex++
        this.currentLine = this.lineBuffer.getLineOptional(this.lineIndex).orElse("")
    }

    private fun previousCharIndex() = this.charIndex - 1u

    private fun gotMoreLines() = this.lineIndex <= this.lineBuffer.lineCount()
}

private fun CharSequence.getOptional(index: UInt): Optional<Char> {
    if (index.toInt() > this.lastIndex)
        return Optional.empty()

    return Optional.of(
        this[index.toInt()]
    )
}