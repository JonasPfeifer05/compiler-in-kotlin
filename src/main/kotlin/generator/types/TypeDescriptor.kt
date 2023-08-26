package generator.types

import generator.ASMBuilder

sealed class TypeDescriptor {
    abstract fun sizeOf(): UInt

    abstract fun copyTo(offsetTo: UInt, offsetFrom: UInt, asmBuilder: ASMBuilder)
}