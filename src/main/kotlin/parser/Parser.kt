package parser

import errors.parser.FoundUnexpectedTokenException
import errors.parser.RanOutOfTokensButExpectedTokenException
import errors.parser.UnexpectedTokenException
import general.LineBuffer
import lexer.Token
import lexer.TokenFlag
import parser.nodes.*
import parser.statements.ExitStatement
import parser.statements.Statement
import java.util.Optional

class Parser(private val lineBuffer: LineBuffer, private val tokens: List<Token>) {
    private var tokenIndex = 0u

    fun parseProgram(): List<Statement> {
        val nodes: MutableList<Statement> = mutableListOf()

        while (this.peekToken().isPresent) {
            nodes.add(parseStatement())
        }

        return nodes
    }

    private fun parseStatement(): Statement {
        val startToken = this.consumeToken()

        if (startToken.flag == TokenFlag.Exit) {
            this.expectNextTokenFlag(TokenFlag.OpenBracket)

            val expression = this.parseExpression()

            this.expectNextTokenFlag(TokenFlag.ClosedBracket)
            this.expectNextTokenFlag(TokenFlag.Semicolon)

            return ExitStatement(expression)
        } else throw UnexpectedTokenException(startToken)
    }

    private fun parseExpression(): ExpressionNode {
        return NumberExpressionNode(
            this.expectNextTokenFlag(TokenFlag.Number).value
        )
    }

    private fun expectNextTokenFlag(vararg expectedFlags: TokenFlag): Token {
        if (this.peekToken().isEmpty) throw RanOutOfTokensButExpectedTokenException(expectedFlags, this.previousToken().tokenLocation.lineIndex, this.lineBuffer)

        val token = this.consumeToken()
        if (!expectedFlags.contains(token.flag)) throw FoundUnexpectedTokenException(token, expectedFlags, this.lineBuffer)

        return token
    }

    private fun consumeToken(): Token {
        val token = this.tokens[this.tokenIndex.toInt()]

        this.tokenIndex++
        return token
    }

    private fun previousToken(): Token {
        return this.tokens[this.tokenIndex.toInt() - 1]
    }

    private fun peekToken(): Optional<Token> {
        if (this.tokenIndex.toInt() == tokens.size) return Optional.empty()

        return Optional.of(this.tokens[this.tokenIndex.toInt()])
    }
}