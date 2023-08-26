package generator

class VirtualStackDescriptor {
    private var currentSize: Int = 0

    fun grow(bytes: Int) {
        currentSize += bytes
    }

    fun shrink(bytes: Int) {
        currentSize -= bytes
    }

    fun pushNumber() {
        this.currentSize += 8
    }

    fun popNumber() {
        this.currentSize -= 8
    }

    fun getCurrentStackSize(): Int = this.currentSize
}