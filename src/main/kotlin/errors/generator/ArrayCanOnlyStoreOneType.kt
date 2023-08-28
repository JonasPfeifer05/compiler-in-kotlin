package errors.generator

import generator.types.TypeDescriptor
import java.lang.Exception

class ArrayCanOnlyStoreOneType(expected: TypeDescriptor, got: TypeDescriptor):
    Exception("An array can only store values of one type! Expected '$expected' but got '$got'!")