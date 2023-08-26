package errors.generator

import generator.types.TypeDescriptor
import lexer.TokenFlag

class CrossOperationException(operator: TokenFlag, typeLeft: TypeDescriptor, typeRight: TypeDescriptor):
    Exception("Cannot apply '${operator.getSymbol()}' between '$typeLeft' and '$typeRight'!")