import kotlin.reflect.KClass
import kotlin.reflect.full.*

enum class MyEnum(str: String) {
    TEST_1("test1")
}

class MyClass {
    val prop: MyEnum = MyEnum.TEST_1
}

fun getEnumValues(enumClass: KClass<out Enum<*>>): Array<out Enum<*>> = enumClass.java.enumConstants

val ktype = MyClass::class.memberProperties.first().returnType
if(ktype.isSubtypeOf(Enum::class.starProjectedType)) {
    println("is subtype")

    @Suppress("UNCHECKED_CAST") val values = getEnumValues(ktype.classifier as KClass<Enum<*>>)

    println(values)
}