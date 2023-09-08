package lexer

import errors.lexer.NonTerminatedCharException
import errors.lexer.NonTerminatedStringException
import errors.lexer.UnknownCharException
import general.LineBuffer
import java.util.Optional
import kotlin.jvm.optionals.getOrDefault

val UPPER_LETTERS_RANGE = 'A'..'Z'
val LOWER_LETTERS_RANGE = 'a'..'z'
val NUMBER_RANGE = '0'..'9'

val EMPTY_CHARACTERS = charArrayOf(' ', '\t')

class Lexer(private val lineBuffer: LineBuffer) {
    private var lineIndex: Int = 0
    private var charIndex: Int = 0
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
            ':' -> Pair(":", TokenFlag.Colon)
            ',' -> Pair(",", TokenFlag.Comma)
            '(' -> Pair("(", TokenFlag.OpenParent)
            ')' -> Pair(")", TokenFlag.ClosedParent)
            '[' -> Pair("[", TokenFlag.OpenBracket)
            ']' -> Pair("]", TokenFlag.ClosedBracket)
            '=' -> Pair("=", TokenFlag.Assign)
            '+' -> Pair("+", TokenFlag.Plus)
            '-' -> Pair("-", TokenFlag.Minus)
            '*' -> Pair("*", TokenFlag.Mul)
            '/' -> Pair("/", TokenFlag.Div)
            '&' -> Pair("&", TokenFlag.And)
            '"' -> {
                Pair(this.readString(tokenStartIndex), TokenFlag.StringLiteral)
            }
            "'"[0] -> {
                var value: String = this.consumeChar().toString()
                if (this.peekChar().isPresent && this.peekChar().get() != "'"[0]) {
                    value += this.consumeChar()
                }
                if (this.peekChar().isEmpty || this.consumeChar() != "'"[0])
                    throw NonTerminatedCharException(TokenLocation(this.lineIndex, tokenStartIndex..this.charIndex), this.lineBuffer.getLineOptional(this.lineIndex).get())
                Pair(value, TokenFlag.CharLiteral)
            }
            in LOWER_LETTERS_RANGE, in UPPER_LETTERS_RANGE -> {
                val value = currentChar + this.readMatchingSequence { this in LOWER_LETTERS_RANGE || this in NUMBER_RANGE || this in UPPER_LETTERS_RANGE }
                when (value) {
                    "exit" -> Pair(value, TokenFlag.Exit)
                    "let" -> Pair(value, TokenFlag.Let)
                    "print" -> Pair(value, TokenFlag.Print)
                    "u64" -> Pair(value, TokenFlag.U64Type)
                    "string" -> Pair(value, TokenFlag.StringType)
                    "char" -> Pair(value, TokenFlag.CharType)
                    else -> Pair(value, TokenFlag.IdentifierLiteral)
                }
            }
            in NUMBER_RANGE -> Pair(
                currentChar + this.readMatchingSequence { this in NUMBER_RANGE },
                TokenFlag.NumberLiteral,
            )
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

    private fun readString(tokenStartIndex: Int): String {
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
        this.charIndex = 0
        this.lineIndex++
        this.currentLine = this.lineBuffer.getLineOptional(this.lineIndex).orElse("")
    }

    private fun previousCharIndex() = this.charIndex - 1

    private fun gotMoreLines() = this.lineIndex <= this.lineBuffer.lineCount()
}

private fun CharSequence.getOptional(index: Int): Optional<Char> {
    if (index > this.lastIndex)
        return Optional.empty()

    return Optional.of(
        this[index]
    )
}