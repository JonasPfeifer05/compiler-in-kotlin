package generator

sealed class DataSource

class Register(private val name: String): DataSource() {
    companion object {
        val Rax = Register("rax")
        val PrimaryCalculation = Rax
        val Rbx = Register("rbx")
        val SecondaryCalculation = Rbx
        val Rdx = Register("rdx")
        val Rsp = Register("rsp")
        val Rdi = Register("rdi")
        val Rsi = Register("rsi")
    }

    override fun toString(): String {
        return this.name
    }
}

class ConstantValue(val value: Int): DataSource() {
    override fun toString(): String {
        return this.value.toString()
    }
}

class Offset(val from: Register, private val offset: DataSource): DataSource() {
    override fun toString(): String {
        return "[$from + $offset]"
    }
}

class AddressFrom(val from: Register): DataSource() {
    override fun toString(): String {
        return "[$from]"
    }
}