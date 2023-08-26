package generator.types

import generator.ASMBuilder

data object U64Descriptor : TypeDescriptor() {
    override fun sizeOf(): UInt = 8u
    override fun copyTo(offsetTo: UInt, offsetFrom: UInt, asmBuilder: ASMBuilder) {
        asmBuilder.mov(
            "rax",
            asmBuilder.pointerWithOffset("rsp", offsetFrom.toInt())
        )
        asmBuilder.mov(
            asmBuilder.pointerWithOffset("rsp", offsetTo.toInt()),
            "rax"
        )
    }
}