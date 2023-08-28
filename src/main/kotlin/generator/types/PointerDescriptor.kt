package generator.types

import generator.ASMBuilder
import generator.DataSource
import generator.Register

class PointerDescriptor(val pointsTo: TypeDescriptor): TypeDescriptor() {
    override fun sizeOf(): Int = 8

    override fun copyTo(to: DataSource, from: DataSource, asmBuilder: ASMBuilder) {
        asmBuilder.mov(
            Register.Rbx, from
        )
        asmBuilder.mov(
            to, Register.Rbx
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PointerDescriptor

        return pointsTo == other.pointsTo
    }

    override fun hashCode(): Int {
        return pointsTo.hashCode()
    }

    override fun toString(): String {
        return "&$pointsTo"
    }
}