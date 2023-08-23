import general.LineBuffer
import generator.ASMGenerator
import lexer.Lexer
import parser.Parser
import utils.readResource
import java.io.File
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

fun main() {
    val durationInMillis = measureTimeMillis {
        try {
            val programLineBuffer = LineBuffer(readResource("script.he"))

            val lexer = Lexer(programLineBuffer)
            val tokens = lexer.getTokens()

            val parser = Parser(programLineBuffer, tokens)
            val statements = parser.parseProgram()

            val asmGenerator = ASMGenerator(statements)
            val assembly = asmGenerator.generateAssembly()

            File("asm/program.asm").printWriter().use {
                it.print(assembly)
            }
        } catch (e: Exception) {
            System.err.println("An ERROR occurred while compiling your program!")
            System.err.println(e.message)
            exitProcess(1)
        }
    }
    println("Compilation took ${durationInMillis / 1000.0}s")
}