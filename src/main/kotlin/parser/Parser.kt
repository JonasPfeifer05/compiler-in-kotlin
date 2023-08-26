package parser

import errors.generator.InvalidArrayLengthException
import errors.parser.FoundUnexpectedTokenException
import errors.parser.RanOutOfTokensButExpectedTokenException
import errors.parser.UnexpectedTokenException
import general.LineBuffer
import general.unreachable
import generator.types.ArrayDescriptor
import generator.types.StringDescriptor
import generator.types.TypeDescriptor
import generator.types.U64Descriptor
import lexer.Token
import lexer.TokenFlag
import parser.nodes.*
import parser.statements.*
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
                this.expectNextTokenFlag(TokenFlag.OpenParent)

                val expression = this.parseExpression()

                this.expectNextTokenFlag(TokenFlag.ClosedParent)
                this.expectNextTokenFlag(TokenFlag.Semicolon)

                return ExitStatement(expression)
            }

            TokenFlag.Let -> {
                val name = this.expectNextTokenFlag(TokenFlag.IdentifierLiteral).value

                val type = this.parseType()

                val token = this.expectNextTokenFlag(TokenFlag.Assign, TokenFlag.Semicolon)

                if (token.flag == TokenFlag.Semicolon)
                    return LetStatement(name, type, null)

                val expression = this.parseExpression()

                this.expectNextTokenFlag(TokenFlag.Semicolon)

                return LetStatement(name, type, expression)
            }

            TokenFlag.Print -> {
                this.expectNextTokenFlag(TokenFlag.OpenParent)

                val expression = this.parseExpression()

                this.expectNextTokenFlag(TokenFlag.ClosedParent)
                this.expectNextTokenFlag(TokenFlag.Semicolon)

                return PrintStatement(expression)
            }

            TokenFlag.IdentifierLiteral -> {
                val name = startToken.value

                this.expectNextTokenFlag(TokenFlag.Assign)

                val expression = this.parseExpression()

                this.expectNextTokenFlag(TokenFlag.Semicolon)

                return AssignStatement(name, expression)
            }

            else -> throw UnexpectedTokenException(startToken)
        }
    }

    private fun parseType(): TypeDescriptor {
        this.expectNextTokenFlag(TokenFlag.Colon)

        var descriptor: TypeDescriptor
        val type = this.expectNextTokenFlag(TokenFlag.U64Type, TokenFlag.StringType)

        descriptor = when (type.flag) {
            TokenFlag.U64Type -> U64Descriptor()
            TokenFlag.StringType -> StringDescriptor(5)
            else -> unreachable()
        }

        while (true) {

            val peek = isPeekCertainTokenFlag(TokenFlag.OpenBracket)
            if (peek.isPresent) {
                this.consumeToken()
                val length = this.expectNextTokenFlag(TokenFlag.NumberLiteral).value.toInt()
                this.expectNextTokenFlag(TokenFlag.ClosedBracket)

                if (length <= 0) throw InvalidArrayLengthException(length)

                descriptor = ArrayDescriptor(descriptor, length)
            } else break
        }

        return descriptor
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
        val token = this.expectNextTokenFlag(
            TokenFlag.IdentifierLiteral,
            TokenFlag.NumberLiteral,
            TokenFlag.OpenParent,
            TokenFlag.StringLiteral,
            TokenFlag.OpenBracket,
        )

        val left = when (token.flag) {
            TokenFlag.IdentifierLiteral -> IdentifierLiteralExpressionNode(token.value)
            TokenFlag.NumberLiteral -> NumberLiteralExpressionNode(token.value)
            TokenFlag.OpenParent -> parseEnclosedExpression()
            TokenFlag.StringLiteral -> StringLiteralExpressionNode(token.value)
            TokenFlag.OpenBracket -> parseArray()
            else -> unreachable()
        }

        val peek = this.isPeekCertainTokenFlag(TokenFlag.OpenBracket)
        if (peek.isPresent) {
            this.consumeToken()
            val index = this.parseTerm()
            this.expectNextTokenFlag(TokenFlag.ClosedBracket)
            return AccessExpressionNode(left, index)
        }

        return left
    }

    private fun parseArray(): ExpressionNode {
        val values: MutableList<ExpressionNode> = mutableListOf()
        while (true) {
            values.add(this.parseTerm())
            val comma = this.isPeekCertainTokenFlag(TokenFlag.Comma)
            if (comma.isEmpty) break
            this.consumeToken()
        }
        this.expectNextTokenFlag(TokenFlag.ClosedBracket)
        return ArrayExpressionNode(values)
    }

    private fun parseEnclosedExpression(): ExpressionNode {
        val value = this.parseTerm()

        this.expectNextTokenFlag(TokenFlag.ClosedParent)

        return BracketExpressionNode(value)
    }

    private fun isPeekCertainTokenFlag(vararg expectedFlags: TokenFlag): Optional<Token> {
        if (this.peekToken().isEmpty) return Optional.empty()

        if (expectedFlags.contains(this.peekToken().get().flag)) return this.peekToken()
        return Optional.empty()
    }

    private fun expectNextTokenFlag(vararg expectedFlags: TokenFlag): Token {
        if (this.peekToken().isEmpty) throw RanOutOfTokensButExpectedTokenException(
            expectedFlags,
            this.previousToken().tokenLocation.lineIndex,
            this.lineBuffer
        )

        val token = this.consumeToken()
        if (!expectedFlags.contains(token.flag)) throw FoundUnexpectedTokenException(
            token,
            expectedFlags,
            this.lineBuffer
        )

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