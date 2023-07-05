import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.extensions.AnnotationFqn
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.FirDeclarationPredicateRegistrar
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId

class ExampleFirDeclarationGenerationExtension(session: FirSession): FirDeclarationGenerationExtension(session) {

    override fun getTopLevelCallableIds(): Set<CallableId> {
        return super.getTopLevelCallableIds()
    }

    override fun getTopLevelClassIds(): Set<ClassId> {
        val matched = session.predicateBasedProvider.getSymbolsByPredicate(predicate)
        return matched.mapNotNull { (it as? FirClassLikeSymbol<*>)?.classId }.toSet()
    }

    override fun generateTopLevelClassLikeDeclaration(classId: ClassId): FirClassLikeSymbol<*>? {
        return super.generateTopLevelClassLikeDeclaration(classId)
    }

    override fun FirDeclarationPredicateRegistrar.registerPredicates() {
        register(
            predicate
        )
    }

    companion object {
        val predicate = LookupPredicate.create {
            annotatedOrUnder(AnnotationFqn("ExampleAnnotation"))
        }
    }
}
