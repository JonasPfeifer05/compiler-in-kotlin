package generator.types

import generator.ASMBuilder
import generator.AddressFrom

abstract class TypeDescriptor {
    abstract fun sizeOf(): Int

    abstract fun copyTo(to: AddressFrom, from: AddressFrom, asmBuilder: ASMBuilder)
}