package errors.generator

import generator.types.TypeDescriptor
import lexer.TokenFlag

class IllegalOperationException(operation: TokenFlag, typeLeft: TypeDescriptor, typeRight: TypeDescriptor):
    Exception("Illegal operator '${operation.getSymbol()}' between '$typeLeft' and '$typeRight'!")