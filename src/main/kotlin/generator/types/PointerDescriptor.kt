package generator.types

import generator.ASMBuilder

class PointerDescriptor(val pointsTo: TypeDescriptor): TypeDescriptor() {
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

    override fun copyTo(offsetTo: Int, from: String, asmBuilder: ASMBuilder) {
        asmBuilder.mov(
            "rax",
            "[$from]"
        )
        asmBuilder.mov(
            asmBuilder.pointerWithOffset("rsp", offsetTo),
            "rax"
        )
    }


}