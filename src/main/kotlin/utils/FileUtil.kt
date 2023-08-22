package utils;

import java.io.File
import java.nio.charset.Charset

fun readResource(path: String): String {
    val pathCorrected = if (path.startsWith("/")) path else "/$path";
    val resource = {}.javaClass.getResource(pathCorrected) ?: throw UnknownResourceException(pathCorrected);
    return resource.readText();
}

class UnknownResourceException(path: String): Exception("An nonexistent resource with the path '$path' was requested!");