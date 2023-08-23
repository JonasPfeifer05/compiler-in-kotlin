package general
fun unreachable(): Nothing = throw UnreachableException()

class UnreachableException: Exception("This code should be unreachable")