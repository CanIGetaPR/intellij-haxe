/*
 * Copyright 2018-2018 Ilya Malanin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.plugins.haxe.ide.quickfix;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.plugins.haxe.HaxeBundle;
import com.intellij.plugins.haxe.model.HaxeDocumentModel;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HaxeRemoveExcessiveArgumentsFix extends HaxeFixAndIntentionAction {
  private final List<? extends PsiElement> arguments;

  public HaxeRemoveExcessiveArgumentsFix(List<? extends PsiElement> arguments, PsiElement firstExcessiveArgument) {
    super(firstExcessiveArgument);
    this.arguments = arguments;
  }

  @Override
  public void invoke(@NotNull Project project,
                     @NotNull PsiFile file,
                     @Nullable Editor editor,
                     @NotNull PsiElement startElement,
                     @NotNull PsiElement endElement) {
    int firstExcessiveArgumentIndex = arguments.indexOf(startElement);
    int lastArgumentEndOffset = arguments.get(arguments.size() - 1).getTextRange().getEndOffset();
    final TextRange textRange;

    if (firstExcessiveArgumentIndex == 0) {
      textRange = new TextRange(arguments.get(0).getTextOffset(), lastArgumentEndOffset);
    } else {
      textRange = new TextRange(arguments.get(firstExcessiveArgumentIndex - 1).getTextRange().getEndOffset(), lastArgumentEndOffset);
    }

    HaxeDocumentModel.fromElement(startElement).replaceElementText(textRange, "");
  }

  @Override
  public boolean isAvailable(@NotNull Project project,
                             @NotNull PsiFile file,
                             @NotNull PsiElement startElement,
                             @NotNull PsiElement endElement) {

    return arguments.indexOf(startElement) >= 0;
  }

  @NotNull
  @Override
  public String getText() {
    return HaxeBundle.message("haxe.quickfix.remove.excessive.arguments");
  }
}