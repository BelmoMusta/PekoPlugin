package musta.belmo.peko.plugins.action;

import com.intellij.compiler.cache.git.GitRepositoryUtil;
import com.intellij.execution.Executor;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.xml.XmlFileImpl;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlTag;
import com.intellij.terminal.TerminalShellCommandHandler;
import com.intellij.vcs.console.VcsConsoleView;
import i.E.V;
import musta.belmo.peko.plugins.ast.Transformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class AbstractAction extends AnAction {


    @Override
    public void update(AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {

        FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
        if (event.getProject() != null) {
            final String newVersion = JOptionPane.showInputDialog("Enter new version");

            PsiElement[] children = event.getData(CommonDataKeys.PSI_ELEMENT).getChildren();
            for (PsiElement child : children) {
                if (child instanceof XmlFileImpl
                        && ((XmlFileImpl) child).getName().equals("pom.xml")) {
                    XmlFileImpl xmlFile = (XmlFileImpl) child;
                    XmlDocument document = xmlFile.getDocument();
                    if (document == null) {
                        return;
                    }

                    XmlTag rootTag = document.getRootTag();
                    if (rootTag == null) {
                        return;
                    }

                    XmlTag properties = rootTag.findFirstSubTag("properties");
                    if (properties == null) {
                        return;
                    }
                    XmlTag revision = properties.findFirstSubTag("revision");
                    if (revision != null) {
                        Runnable runnable = () -> {
                            if (revision.getValue().getTextElements().length > 0)
                                revision.getValue().getTextElements()[0].setValue(newVersion);
                            fileDocumentManager.saveDocument((Document) document);

                        };
                        ApplicationManager.getApplication().runWriteAction(getRunnableWrapper(runnable, event.getProject()));
                    }
                    return;
                }
            }
        }
    }


    private Runnable getRunnableWrapper(final Runnable runnable, Project project) {
        return () -> CommandProcessor.getInstance().executeCommand(project, runnable, "cut", ActionGroup.EMPTY_GROUP);
    }

    protected abstract Transformer getTransformer();
}
