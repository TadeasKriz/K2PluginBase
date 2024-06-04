import kotlinx.coroutines.flow.MutableStateFlow

@ExampleAnnotation("Hello World!")
@AutoFactory
class ExampleClass(
    val firstPart: String,
    @Provided
    val secondPart: String,
) {

    val mutableStateFlow = MutableStateFlow("$firstPart $secondPart")
}
