package errors.generator

import generator.types.TypeDescriptor
import java.lang.Exception

class WrongParameterTypeException(expected: TypeDescriptor, got: TypeDescriptor):
    Exception("Function expected an parameter of type '$expected' but got '$got'!")