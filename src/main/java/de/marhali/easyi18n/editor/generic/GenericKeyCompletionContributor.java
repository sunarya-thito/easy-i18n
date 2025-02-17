package de.marhali.easyi18n.editor.generic;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.*;
import com.intellij.psi.*;
import com.intellij.psi.xml.*;
import de.marhali.easyi18n.editor.KeyCompletionProvider;

/**
 * Translation key completion for generic languages which support {@link PsiLiteralValue}.
 * @author marhali
 */
public class GenericKeyCompletionContributor extends CompletionContributor {

    public GenericKeyCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(PlainTextTokenTypes.PLAIN_TEXT),
                new KeyCompletionProvider());
        extend(CompletionType.BASIC, PlatformPatterns.psiElement().inside(XmlElement.class),
                new KeyCompletionProvider());
        extend(CompletionType.BASIC, PlatformPatterns.psiElement().inside(PsiLiteralValue.class),
                new KeyCompletionProvider());
    }
}