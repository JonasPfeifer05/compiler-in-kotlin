package general

class Register(val register: String) {
    companion object {
        val PrimaryCalculation: Register = Register("rax")
        val SecondaryCalculation: Register = Register("rbx")
    }
}