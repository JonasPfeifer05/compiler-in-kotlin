package generator

enum class MemorySizes {
    Byte,
    Word,
    DWord,
    QWord;

    override fun toString(): String {
        return when (this) {
            Byte -> "BYTE"
            Word -> "WORD"
            DWord -> "DWORD"
            QWord -> "QWORD"
        }
    }
}