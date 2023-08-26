package generator

import generator.types.TypeDescriptor

class VariableLookupTable(private val stack: VirtualStackDescriptor) {
    private val variableToStackPointer: HashMap<String, Pair<Int, TypeDescriptor>> = HashMap()

    fun doesVariablesExits(name: String): Boolean = variableToStackPointer.containsKey(name)

    fun registerNewVariable(name: String, type: TypeDescriptor) {
        this.variableToStackPointer[name] = Pair(
            this.stack.getCurrentStackSize(),
            type
        )
    }

    fun getVariable(name: String): Pair<Int, TypeDescriptor> {
        return this.variableToStackPointer[name]!!
    }
}