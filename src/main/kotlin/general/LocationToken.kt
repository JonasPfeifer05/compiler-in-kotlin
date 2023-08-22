package general

data class LocationToken(val lineIndex: UInt, val sectionIndex: UIntRange) {
    constructor(lineIndex: UInt, singleCharacterIndex: UInt): this(lineIndex, singleCharacterIndex..singleCharacterIndex);
}
