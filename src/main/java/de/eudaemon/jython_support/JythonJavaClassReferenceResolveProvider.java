package de.eudaemon.jython_support;

import java.util.List;

import com.intellij.psi.PsiClass;
import com.intellij.psi.impl.java.stubs.index.JavaFullClassNameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.python.psi.PyFromImportStatement;
import com.jetbrains.python.psi.PyImportElement;
import com.jetbrains.python.psi.PyQualifiedExpression;
import com.jetbrains.python.psi.impl.ResolveResultList;
import com.jetbrains.python.psi.resolve.PyOverridingReferenceResolveProvider;
import com.jetbrains.python.psi.resolve.RatedResolveResult;
import com.jetbrains.python.psi.types.TypeEvalContext;

import org.jetbrains.annotations.NotNull;

public class JythonJavaClassReferenceResolveProvider
        implements PyOverridingReferenceResolveProvider {
    @Override
    public @NotNull List<RatedResolveResult> resolveName(
            @NotNull PyQualifiedExpression expression, @NotNull TypeEvalContext context) {
        if (PsiTreeUtil.getParentOfType(expression, PyImportElement.class) != null) {
            PyFromImportStatement importStatement =
                    PsiTreeUtil.getParentOfType(expression, PyFromImportStatement.class);
            if (importStatement == null) {
                return List.of();
            }
            String pkg = importStatement.getImportSource().getName();
            ResolveResultList results = new ResolveResultList();
            for (PsiClass targetJavaClass :
                    JavaFullClassNameIndex.getInstance()
                            .get(
                                    "%s.%s".formatted(pkg, expression.getReferencedName()),
                                    expression.getProject(),
                                    GlobalSearchScope.allScope(expression.getProject()))) {}
        }

        //        Collection<PsiClass> psiClasses =
        //                JavaFullClassNameIndex.getInstance()
        //                        .get(
        //                                pkg.append(className).join("."),
        //                                myElement.getProject(),
        //                                GlobalSearchScope.allScope(myElement.getProject()));
        return List.of();
    }
}
