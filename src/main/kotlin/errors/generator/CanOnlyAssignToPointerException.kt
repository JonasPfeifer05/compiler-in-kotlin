package errors.generator

class CanOnlyAssignToPointerException:
    Exception("You can only assign a value to an pointer (lvalue)!")