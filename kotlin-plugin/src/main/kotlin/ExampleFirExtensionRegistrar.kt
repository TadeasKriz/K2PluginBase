import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class ExampleFirExtensionRegistrar: FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::ExampleFirAdditionalCheckersExtension

        +::ExampleFirDeclarationGenerationExtension
    }
}
