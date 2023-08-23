package generator

class VirtualStackDescriptor {
    private var currentSize: UInt = 0u

    fun pushNumber() {
        this.currentSize += 8u
    }

    fun popNumber() {
        this.currentSize -= 8u
    }

    fun getCurrentStackSize(): UInt = this.currentSize
}