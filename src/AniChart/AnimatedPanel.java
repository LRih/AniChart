package AniChart;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Provides support for animation.
 */
public class AnimatedPanel extends JPanel implements ActionListener
{
    //========================================================================= VARIABLES
    private static final int ANIMATION_DURATION = 30;
    private static final int ANIMATION_FPS = 60;

    private int _aniProgress = 0;
    private final Timer _timer;

    //========================================================================= INITIALIZE
    public AnimatedPanel()
    {
        _timer = new Timer(1000 / ANIMATION_FPS, this);
    }

    //========================================================================= FUNCTIONS
    protected final void startAnimation()
    {
        _aniProgress = ANIMATION_DURATION;
        _timer.start();
    }

    //========================================================================= PROPERTIES
    protected final float aniProgress()
    {
        return 1 - _aniProgress / (float)ANIMATION_DURATION;
    }

    //========================================================================= EVENTS
    public final void actionPerformed(ActionEvent e)
    {
        _aniProgress--;

        if (_aniProgress == 0)
            _timer.stop();

        onAniProgressChanged();
    }

    protected void onAniProgressChanged()
    {
    }
}
