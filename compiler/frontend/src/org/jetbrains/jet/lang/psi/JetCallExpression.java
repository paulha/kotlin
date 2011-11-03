package org.jetbrains.jet.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.JetNodeTypes;

import java.util.Collections;
import java.util.List;

/**
 * @author max
 */
public class JetCallExpression extends JetExpression implements JetCallElement {
    public JetCallExpression(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public void accept(@NotNull JetVisitorVoid visitor) {
        visitor.visitCallExpression(this);
    }

    @Override
    public <R, D> R accept(@NotNull JetVisitor<R, D> visitor, D data) {
        return visitor.visitCallExpression(this, data);
    }

    @Override
    @Nullable
    public JetExpression getCalleeExpression() {
        return findChildByClass(JetExpression.class);
    }

    @Override
    @Nullable
    public JetValueArgumentList getValueArgumentList() {
        return (JetValueArgumentList) findChildByType(JetNodeTypes.VALUE_ARGUMENT_LIST);
    }

    @Nullable
    public JetTypeArgumentList getTypeArgumentList() {
        return (JetTypeArgumentList) findChildByType(JetNodeTypes.TYPE_ARGUMENT_LIST);
    }

    @Override
    @NotNull
    public List<JetExpression> getFunctionLiteralArguments() {
        JetExpression calleeExpression = getCalleeExpression();
        if (calleeExpression instanceof JetFunctionLiteralExpression) {
            List<JetExpression> result = new SmartList<JetExpression>();
            ASTNode treeNext = calleeExpression.getNode().getTreeNext();
            while (treeNext != null) {
                PsiElement psi = treeNext.getPsi();
                if (psi instanceof JetFunctionLiteralExpression) {
                    result.add((JetFunctionLiteralExpression) psi);
                }
                else if (psi instanceof JetLabelQualifiedExpression) {
                    JetLabelQualifiedExpression labelQualifiedExpression = (JetLabelQualifiedExpression) psi;
                    JetExpression labeledExpression = labelQualifiedExpression.getLabeledExpression();
                    if (labeledExpression instanceof JetFunctionLiteralExpression) {
                        result.add(labeledExpression);
                    }
                }
                treeNext = treeNext.getTreeNext();
            }
            return result;
        }
        return findChildrenByType(JetNodeTypes.FUNCTION_LITERAL_EXPRESSION);
    }

    @Override
    @NotNull
    public List<? extends ValueArgument> getValueArguments() {
        JetValueArgumentList list = getValueArgumentList();
        return list != null ? list.getArguments() : Collections.<JetValueArgument>emptyList();
    }

    @NotNull
    public List<JetTypeProjection> getTypeArguments() {
        JetTypeArgumentList list = getTypeArgumentList();
        return list != null ? list.getArguments() : Collections.<JetTypeProjection>emptyList();
    }
}
