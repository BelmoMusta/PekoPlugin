package musta.belmo.peko.plugins;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;

/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "org.intellij.sdk.settings.AppSettingsState",
        storages = @Storage("SdkSettingsPlugin.xml")
)
public class AppSettingsState implements PersistentStateComponent<AppSettingsState> {

    public String userId = "John Q. Public";
    public boolean ideaStatus = false;

    public static AppSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(AppSettingsState.class);
    }


    @Override
    public AppSettingsState getState() {
        return this;
    }

    @Override
    public void loadState( AppSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}