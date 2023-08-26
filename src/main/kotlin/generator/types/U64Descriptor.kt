package generator.types

import generator.ASMBuilder

class U64Descriptor : TypeDescriptor() {
    override fun sizeOf(): Int = 8
    override fun copyTo(offsetTo: Int, offsetFrom: Int, asmBuilder: ASMBuilder) {
        asmBuilder.mov(
            "rax",
            asmBuilder.pointerWithOffset("rsp", offsetFrom)
        )
        asmBuilder.mov(
            asmBuilder.pointerWithOffset("rsp", offsetTo),
            "rax"
        )
    }

    override fun toString(): String {
        return "U64"
    }
}