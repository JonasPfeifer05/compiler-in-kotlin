import generator.Generator
import lexer.Lexer
import parser.Parser
import utils.readResource
import java.io.File

fun main(args: Array<String>) {
    val lexer = Lexer(readResource("script.he"))
    val tokens = lexer.getTokens();
    println(tokens);

    val parser = Parser(tokens);
    val program = parser.parseProgram();
    println(program);

    val generator = Generator(program);
    val assembly = generator.generateAssembly();

    File("asm/program.asm").printWriter().use {
        it.print(assembly);
    }
}