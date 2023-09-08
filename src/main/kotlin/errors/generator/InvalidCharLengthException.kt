package errors.generator

class InvalidCharLengthException(private val length: Int):
    Exception("Chars cannot have the length of $length!") {
}