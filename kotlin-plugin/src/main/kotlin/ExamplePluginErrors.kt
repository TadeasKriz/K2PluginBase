import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.diagnostics.SourceElementPositioningStrategies
import org.jetbrains.kotlin.diagnostics.error2
import org.jetbrains.kotlin.diagnostics.warning1

object ExamplePluginErrors {
    val MISSING_EXAMPLE_ANNOTATION by error2<PsiElement, String, String>()
    val PASCAL_NOT_WELCOME by warning1<PsiElement, String>(SourceElementPositioningStrategies.DECLARATION_NAME)
}
