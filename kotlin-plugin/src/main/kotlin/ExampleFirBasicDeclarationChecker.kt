import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirBasicDeclarationChecker
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.fir.declarations.FirDeclaration
import org.jetbrains.kotlin.fir.declarations.utils.nameOrSpecialName
import org.jetbrains.kotlin.fir.render

class ExampleFirBasicDeclarationChecker: FirBasicDeclarationChecker() {
    override fun check(declaration: FirDeclaration, context: CheckerContext, reporter: DiagnosticReporter) {
        println("[CHECK][DECL]: ${declaration.render()}")

        if (declaration is FirClass) {
            if (declaration.nameOrSpecialName.identifier.first().isUpperCase()) {
                reporter.reportOn(declaration.source,
                    ExamplePluginErrors.PASCAL_NOT_WELCOME, "Class names should begin with lowercase characters, we don't like Pascal here.", context)
            }
        }

    }
}
