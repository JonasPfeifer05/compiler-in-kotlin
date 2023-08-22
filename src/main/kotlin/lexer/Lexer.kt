package lexer

import general.LocationToken
import general.Token
import general.TokenFlag
import java.util.Optional

val LITERAL_RANGE = 'a'..'z';
val NUMBER_RANGE = '0'..'9';

const val NEW_LINE = '\n';
val EMPTY_CHARACTERS = charArrayOf(' ', '\t');
val SKIPABLES = charArrayOf('\r');

class Lexer(private val data: CharArray) {
    // For getting / peeking chars
    private var globalIndex: UInt = 0u;

    // For error creation
    private var lineIndex: UInt = 0u;
    private var characterIndex: UInt = 0u;
    private val currentLineContent = StringBuilder();

    constructor(data: String) : this(data.toCharArray());

    fun getTokens(): List<Token> {
        val tokens: MutableList<Token> = mutableListOf();

        var token = this.getNextToken();
        while (token.isPresent) {
            tokens.add(token.get());
            token = this.getNextToken();
        }

        return tokens.filter { it.flag != TokenFlag.Useless };
    }

    private fun getNextToken(): Optional<Token> {
        val currentCharOptional = this.consumeChar();
        if (currentCharOptional.isEmpty)
            return Optional.empty();

        val currentChar = currentCharOptional.get();

        if (SKIPABLES.contains(currentChar))
            return Optional.of(Token("skipable", TokenFlag.Useless, LocationToken()));

        this.currentLineContent.append(currentChar);

        if (currentChar == NEW_LINE) {
            this.prepareNextLine();
            return Optional.of(Token("\\n", TokenFlag.Useless, LocationToken()));
        }

        this.advanceIndicesToNextCharacter();

        if (EMPTY_CHARACTERS.contains(currentChar))
            return Optional.of(Token("empty", TokenFlag.Useless, LocationToken()));

        val tokenStartIndex = this.characterIndex-1u;
        if (currentChar == ';') {
            return Optional.of(
                Token(";", TokenFlag.Semicolon, LocationToken(this.lineIndex, tokenStartIndex..this.characterIndex))
            )
        } else if (currentChar == '(') {
            return Optional.of(
                Token("(", TokenFlag.OpenBracket, LocationToken(this.lineIndex, tokenStartIndex..this.characterIndex))
            )
        } else if (currentChar == ')') {
            return Optional.of(
                Token(")", TokenFlag.ClosedBracket, LocationToken(this.lineIndex, tokenStartIndex..this.characterIndex))
            )
        } else if (currentChar in LITERAL_RANGE) {
            return Optional.of(
                this.readLiteral(currentChar, tokenStartIndex)
            );
        } else if (currentChar in NUMBER_RANGE) {
            val value = currentChar + this.readMatchingSequence { this in NUMBER_RANGE };
            return Optional.of(
                Token(value, TokenFlag.Number, LocationToken(this.lineIndex, tokenStartIndex..this.characterIndex))
            );
        }

        throw UnknownCharException(currentChar, LocationToken(this.lineIndex, this.characterIndex-1u), this.readCurrentLineToEnd());
    }

    private fun readLiteral(start: Char, tokenStartIndex: UInt): Token {
        val content = start + this.readMatchingSequence { this in LITERAL_RANGE };

        val location = LocationToken(this.lineIndex, tokenStartIndex..this.characterIndex);

        if (content == "exit") {
            return Token(content, TokenFlag.Exit, location);
        }

        return Token(content, TokenFlag.Literal, location);
    }

    private fun readMatchingSequence(doesMatch: Char.() -> Boolean): String {
        val builder = StringBuilder();

        var peekChar = this.peekChar();
        while (peekChar.isPresent && peekChar.get().doesMatch()) {
            val char = this.consumeChar().get();

            this.advanceIndicesToNextCharacter();
            this.currentLineContent.append(char);

            builder.append(char);

            peekChar = this.peekChar();
        }

        return builder.toString();
    }

    private fun consumeChar(): Optional<Char> {
        if (this.globalIndex.toInt() == this.data.size)
            return Optional.empty();

        this.globalIndex++;

        return Optional.of(
            this.data[this.globalIndex.toInt() - 1]
        );
    }

    private fun peekChar(): Optional<Char> {
        if (this.globalIndex.toInt() == this.data.size)
            return Optional.empty();

        return Optional.of(
            this.data[this.globalIndex.toInt()]
        );
    }

    private fun prepareNextLine() {
        this.currentLineContent.clear();
        this.advanceIndicesToNewLine();
    }

    private fun readCurrentLineToEnd(): String {
        var charOptional = consumeChar();
        while (charOptional.isPresent) {
            val char = charOptional.get();

            if (char == NEW_LINE) break;

            currentLineContent.append(char);

            charOptional = consumeChar();
        }

        return this.currentLineContent.toString();
    }

    private fun advanceIndicesToNextCharacter() = this.characterIndex++;
    private fun advanceIndicesToNewLine() {
        this.characterIndex = 0u;
        this.lineIndex++;
    }
}