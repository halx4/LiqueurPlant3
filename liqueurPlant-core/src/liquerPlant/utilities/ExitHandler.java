package liquerPlant.utilities;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ExitHandler extends WindowAdapter  {

    @Override
    public void windowClosing(WindowEvent closeWindowAndExit) {
        System.exit(0);

    }

}