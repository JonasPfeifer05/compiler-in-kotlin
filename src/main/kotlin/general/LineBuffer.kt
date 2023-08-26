package general

import java.util.*

class LineBuffer(programText: String) {
    private val lines: List<String>

    init {
        this.lines = programText.lines()
    }

    fun getLineOptional(lineIndex: Int): Optional<String> {
        if (lineIndex > this.lines.lastIndex)
            return Optional.empty()

        return Optional.of(
            this.lines[lineIndex]
        )
    }

    fun lineCount(): Int = this.lines.size
}