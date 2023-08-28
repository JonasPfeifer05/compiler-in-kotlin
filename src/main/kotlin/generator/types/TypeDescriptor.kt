package generator.types

import generator.ASMBuilder

abstract class TypeDescriptor {
    abstract fun sizeOf(): Int

    abstract fun copyTo(offsetTo: Int, offsetFrom: Int, asmBuilder: ASMBuilder)
    abstract fun copyTo(offsetTo: Int, from: String, asmBuilder: ASMBuilder)
}