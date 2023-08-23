import general.LineBuffer
import generator.Generator
import lexer.Lexer
import parser.Parser
import utils.readResource
import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val durationInMillis = measureTimeMillis {
        val programLineBuffer = LineBuffer(readResource("script.he"));

        val lexer = Lexer(programLineBuffer)
        val tokens = lexer.getTokens();

        val parser = Parser(tokens);
        val program = parser.parseProgram();
        println(program);

        val generator = Generator(program);
        val assembly = generator.generateAssembly();

        File("asm/program.asm").printWriter().use {
            it.print(assembly);
        }
    }
    println("Compilation took ${durationInMillis/1000.0}s");
}