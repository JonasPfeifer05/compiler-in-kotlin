package generator.types

import generator.ASMBuilder
import generator.DataSource

abstract class TypeDescriptor {
    abstract fun sizeOf(): Int

    abstract fun copyTo(to: DataSource, from: DataSource, asmBuilder: ASMBuilder)
}