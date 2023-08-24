package parser

import errors.parser.FoundUnexpectedTokenException
import errors.parser.RanOutOfTokensButExpectedTokenException
import errors.parser.UnexpectedTokenException
import general.LineBuffer
import general.unreachable
import lexer.Token
import lexer.TokenFlag
import parser.nodes.*
import parser.statements.AssignStatement
import parser.statements.ExitStatement
import parser.statements.LetStatement
import parser.statements.Statement
import java.util.*

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

        when (startToken.flag) {
            TokenFlag.Exit -> {
                this.expectNextTokenFlag(TokenFlag.OpenBracket)

                val expression = this.parseExpression()

                this.expectNextTokenFlag(TokenFlag.ClosedBracket)
                this.expectNextTokenFlag(TokenFlag.Semicolon)

                return ExitStatement(expression)
            }
            TokenFlag.Let -> {
                val name = this.expectNextTokenFlag(TokenFlag.Literal).value

                this.expectNextTokenFlag(TokenFlag.Assign)

                val expression = this.parseExpression()

                this.expectNextTokenFlag(TokenFlag.Semicolon)

                return LetStatement(name, expression)
            }
            TokenFlag.Literal -> {
                val name = startToken.value

                this.expectNextTokenFlag(TokenFlag.Assign)

                val expression = this.parseExpression()

                this.expectNextTokenFlag(TokenFlag.Semicolon)

                return AssignStatement(name, expression)
            }
            else -> throw UnexpectedTokenException(startToken)
        }
    }

    private fun parseExpression(): Expression {
        return Expression(this.parseTerm())
    }

    private fun parseTerm(): ExpressionNode {
        var left = this.parseFactor()

        if (peekToken().isEmpty) return left

        while (arrayOf(TokenFlag.Plus, TokenFlag.Minus).contains(peekToken().get().flag)) {
            val operation = this.consumeToken()
            val right = this.parseFactor()

            left = OperationExpressionNode(left, operation.flag, right)
        }

        return left
    }

    private fun parseFactor(): ExpressionNode {
        var left = this.parseSingleValue()

        if (peekToken().isEmpty) return left

        while (arrayOf(TokenFlag.Mul, TokenFlag.Div).contains(peekToken().get().flag)) {
            val operation = this.consumeToken()
            val right = this.parseSingleValue()

            left = OperationExpressionNode(left, operation.flag, right)
        }

        return left
    }

    private fun parseSingleValue(): ExpressionNode {
        val token = this.expectNextTokenFlag(TokenFlag.Literal, TokenFlag.Number, TokenFlag.OpenBracket)

        val left = when (token.flag) {
            TokenFlag.Literal -> LiteralExpressionNode(token.value)
            TokenFlag.Number -> NumberExpressionNode(token.value)
            TokenFlag.OpenBracket -> parseBracket()
            else -> unreachable()
        }

        return left
    }

    private fun parseBracket(): ExpressionNode {
        val value = this.parseTerm()

        this.expectNextTokenFlag(TokenFlag.ClosedBracket)

        return BracketExpressionNode(value)
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