package errors.generator

class InvalidCharLengthException(length: Int):
    Exception("Chars cannot have the length of $length!")