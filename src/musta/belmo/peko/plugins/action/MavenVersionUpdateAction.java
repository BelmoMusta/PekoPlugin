package musta.belmo.peko.plugins.action;

import musta.belmo.peko.plugins.ast.JPAAnnotationsTransformer;
import musta.belmo.peko.plugins.ast.Transformer;

public class MavenVersionUpdateAction extends AbstractAction {
    
    @Override
    protected Transformer getTransformer() {
        return new JPAAnnotationsTransformer();
    }
}