package generator.types

import generator.ASMBuilder

abstract class TypeDescriptor {
    abstract fun sizeOf(): UInt

    abstract fun copyTo(offsetTo: UInt, offsetFrom: UInt, asmBuilder: ASMBuilder)
}