package errors.generator

class UnknownVariableException(name: String):
    Exception("Compiler tried to access unknown variable '$name'!")