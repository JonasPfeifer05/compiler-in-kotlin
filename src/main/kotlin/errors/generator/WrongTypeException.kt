package errors.generator

import generator.types.TypeDescriptor
import java.lang.Exception

class WrongTypeException(expected: TypeDescriptor, actual: TypeDescriptor):
    Exception("The parser expected a value of type '$expected' but got '$actual'!")