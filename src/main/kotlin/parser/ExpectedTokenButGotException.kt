package parser

import general.Token
import general.TokenFlag
import java.util.*

class ExpectedTokenButGotException(got: Token, expected: Array<out TokenFlag>):
    Exception("The parser expected an token of one type from these ${expected.contentToString()} but got a token of type ${got.flag.name} in line ${got.locationToken.lineIndex} from index ${got.locationToken.sectionIndex.first} to ${got.locationToken.sectionIndex.last}") {
}