package general

import java.util.*

class LineBuffer(programText: String) {
    private val lines: List<String>

    init {
        this.lines = programText.lines()
    }

    fun getLineOptional(lineIndex: UInt): Optional<String> {
        if (lineIndex.toInt() > this.lines.lastIndex)
            return Optional.empty()

        return Optional.of(
            this.lines[lineIndex.toInt()]
        )
    }

    fun lineCount(): UInt = this.lines.size.toUInt()
}