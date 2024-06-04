annotation class ExampleAnnotation(val value: String)

@Target(AnnotationTarget.CLASS)
annotation class AutoFactory

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Provided

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class AutoClosure
