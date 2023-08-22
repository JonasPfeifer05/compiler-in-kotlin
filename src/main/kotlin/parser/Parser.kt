package parser

import general.Token
import general.TokenFlag
import parser.nodes.*
import java.util.Optional

class Parser(private val tokens: List<Token>) {
    private var globalIndex = 0u;

    fun parseProgram(): Program {
        val nodes: MutableList<StmtNode> = mutableListOf();

        while (this.peekToken().isPresent) {
            nodes.add(parseStatement())
        }

        return Program(nodes);
    }

    private fun parseStatement(): StmtNode {
        val startToken = this.consumeToken().get();

        if (startToken.flag == TokenFlag.Exit) {
            this.expectNextTokenFlag(TokenFlag.OpenBracket)

            val expression = this.parseExpression();

            this.expectNextTokenFlag(TokenFlag.ClosedBracket)
            this.expectNextTokenFlag(TokenFlag.Semicolon)

            return ExitNode(expression);
        } else throw UnexpectedTokenException(startToken)
    }

    private fun parseExpression(): ExprNode {
        return NumberNode(
            this.expectNextTokenFlag(TokenFlag.Number).value
        )
    }

    private fun expectNextTokenFlag(vararg expectedFlags: TokenFlag): Token {
        val tokenOptional = this.consumeToken();
        if (tokenOptional.isEmpty) throw RanOutOfTokenException();

        val token = tokenOptional.get();
        if (!expectedFlags.contains(token.flag)) throw ExpectedTokenButGotException(token, expectedFlags);

        return token;
    }

    private fun consumeToken(): Optional<Token> {
        if (this.globalIndex.toInt() == tokens.size) return Optional.empty()

        this.globalIndex++;
        return Optional.of(this.tokens[this.globalIndex.toInt() - 1])
    }

    private fun peekToken(): Optional<Token> {
        if (this.globalIndex.toInt() == tokens.size) return Optional.empty()

        return Optional.of(this.tokens[this.globalIndex.toInt()])
    }
}