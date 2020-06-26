package cloudspec.loader

sealed class ModuleLoaderError {
    abstract fun message(): String;

    data class ModuleDirectoryNotFound(val modulePath: String) : ModuleLoaderError() {
        override fun message(): String {
            return "Module directory not found: $modulePath"
        }
    }

    data class ModuleDirectoryAccessDenied(val modulePath: String) : ModuleLoaderError() {
        override fun message(): String {
            return "Not enough permissions to read from module directory: $modulePath"
        }
    }

    data class SyntaxError(val message: String) : ModuleLoaderError() {
        override fun message(): String {
            return "Syntax error: $message"
        }
    }
}