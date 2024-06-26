package parser

import errors.parser.FoundUnexpectedTokenException
import errors.parser.RanOutOfTokensButExpectedTokenException
import errors.parser.UnexpectedTokenException
import general.LineBuffer
import general.unreachable
import generator.types.*
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
        val startToken = this.peekToken().get()

        when (startToken.flag) {
            TokenFlag.Exit -> {
                this.consumeToken()
                this.expectNextTokenFlag(TokenFlag.OpenParent)

                val expression = this.parseExpression()

                this.expectNextTokenFlag(TokenFlag.ClosedParent)
                this.expectNextTokenFlag(TokenFlag.Semicolon)

                return ExitStatement(expression)
            }

            TokenFlag.Let -> {
                this.consumeToken()
                val name = this.expectNextTokenFlag(TokenFlag.IdentifierLiteral).value

                this.expectNextTokenFlag(TokenFlag.Colon)

                val type = this.parseType()

                val token = this.expectNextTokenFlag(TokenFlag.Assign, TokenFlag.Semicolon)

                if (token.flag == TokenFlag.Semicolon)
                    return LetStatement(name, type, null)

                val expression = this.parseExpression()

                this.expectNextTokenFlag(TokenFlag.Semicolon)

                return LetStatement(name, type, expression)
            }

            TokenFlag.Print -> {
                this.consumeToken()
                this.expectNextTokenFlag(TokenFlag.OpenParent)

                val expression = this.parseExpression()

                this.expectNextTokenFlag(TokenFlag.ClosedParent)
                this.expectNextTokenFlag(TokenFlag.Semicolon)

                return PrintStatement(expression)
            }

            TokenFlag.IdentifierLiteral -> {
                val assignee = this.parseSingleValue()

                this.expectNextTokenFlag(TokenFlag.Assign)

                val expression = this.parseExpression()

                this.expectNextTokenFlag(TokenFlag.Semicolon)

                return AssignStatement(assignee, expression)
            }

            else -> throw UnexpectedTokenException(startToken)
        }
    }

    private fun parseType(): TypeDescriptor {
        if (this.isPeekCertainTokenFlag(TokenFlag.And).isPresent) {
            this.consumeToken()
            return PointerDescriptor(parseType())
        }

        if (this.isPeekCertainTokenFlag(TokenFlag.OpenBracket).isPresent) {
            this.consumeToken()
            val length = this.expectNextTokenFlag(TokenFlag.IntegerLiteral).value.toInt()

            this.expectNextTokenFlag(TokenFlag.Comma)

            val descriptor = parseType()

            this.expectNextTokenFlag(TokenFlag.ClosedBracket)

            return ArrayDescriptor(descriptor, length)
        }

        val descriptor: TypeDescriptor
        val type = this.expectNextTokenFlag(TokenFlag.U64Type, TokenFlag.StringType, TokenFlag.CharType)

        descriptor = when (type.flag) {
            TokenFlag.U64Type -> U64Descriptor()
            TokenFlag.CharType -> CharDescriptor()
            // TokenFlag.StringType -> StringDescriptor(5)
            else -> unreachable()
        }

        return descriptor
    }

    private fun parseExpression(): ExpressionNode {
        return this.parseTerm()
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
        val tokenPeek = this.isPeekCertainTokenFlag(TokenFlag.Mul)
        if (tokenPeek.isPresent) {
            this.consumeToken()
            val value = this.parseSingleValue()
            return DerefExpression(value)
        }

        val token = this.expectNextTokenFlag(
            TokenFlag.IdentifierLiteral,
            TokenFlag.IntegerLiteral,
            TokenFlag.OpenParent,
            TokenFlag.StringLiteral,
            TokenFlag.OpenBracket,
            TokenFlag.CharLiteral
        )

        var left = when (token.flag) {
            TokenFlag.IdentifierLiteral -> IdentifierLiteralExpressionNode(token.value)
            TokenFlag.IntegerLiteral -> U64LiteralExpressionNode(token.value)
            TokenFlag.OpenParent -> parseEnclosedExpression()
            //TODO TokenFlag.StringLiteral -> StringLiteralExpressionNode(token.value)
            TokenFlag.OpenBracket -> parseArray()
            TokenFlag.CharLiteral -> CharExpressionNode(token.value)
            TokenFlag.StringLiteral -> stringToCharArrayExpression(token.value)
            else -> unreachable()
        }

        var peek = this.isPeekCertainTokenFlag(TokenFlag.OpenBracket)
        while (peek.isPresent) {
            this.consumeToken()

            val index = this.parseTerm()
            this.expectNextTokenFlag(TokenFlag.ClosedBracket)
            left = AccessExpressionNode(left, index)

            peek = this.isPeekCertainTokenFlag(TokenFlag.OpenBracket)
        }

        return left
    }

    private fun stringToCharArrayExpression(value: String): ExpressionNode {
        val elements: MutableList<CharExpressionNode> = mutableListOf()
        var index = 0

        var charBuffer: String
        while (index < value.length) {
            charBuffer = if (value[index] == '\\') {
                index++
                "\\"
            } else ""

            charBuffer += value[index]

            elements.add(CharExpressionNode(charBuffer))

            index++
        }
        return ArrayExpressionNode(elements)
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