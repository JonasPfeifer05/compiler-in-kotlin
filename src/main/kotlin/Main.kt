import lexer.Lexer
import utils.readResource

fun main(args: Array<String>) {
    val lexer = Lexer(readResource("script.he"))

    println(lexer.getTokens());
}