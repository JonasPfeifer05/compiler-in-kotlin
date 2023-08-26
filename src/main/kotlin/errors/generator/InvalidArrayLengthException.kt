package errors.generator

import java.lang.Exception

class InvalidArrayLengthException(invalidLength: Int):
    Exception("An array cannot have the length $invalidLength!")