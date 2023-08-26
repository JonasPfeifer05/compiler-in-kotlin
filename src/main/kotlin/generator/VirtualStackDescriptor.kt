package generator

class VirtualStackDescriptor {
    private var currentSize: UInt = 0u

    fun grow(bytes: UInt) {
        currentSize += bytes
    }

    fun shrink(bytes: UInt) {
        currentSize -= bytes
    }

    fun pushNumber() {
        this.currentSize += 8u
    }

    fun popNumber() {
        this.currentSize -= 8u
    }

    fun getCurrentStackSize(): UInt = this.currentSize
}