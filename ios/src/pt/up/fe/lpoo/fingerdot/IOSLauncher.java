package pt.up.fe.lpoo.fingerdot;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();

        config.orientationLandscape = true;
        config.orientationPortrait = false;

        config.useAccelerometer = false;
        config.useCompass = false;

        return new IOSApplication(new FingerDot(), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}