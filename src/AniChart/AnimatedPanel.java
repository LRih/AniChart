package AniChart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Provides support for animation.
 */
public abstract class AnimatedPanel extends JPanel implements ActionListener
{
    //========================================================================= VARIABLES
    private static final int ANIMATION_DURATION = 30;
    private static final int ANIMATION_FPS = 60;

    private static final String DEFAULT_TAG = "default";

    private HashMap<String, Integer> _aniProgresses = new HashMap<>();
    private final Timer _timer;

    //========================================================================= INITIALIZE
    public AnimatedPanel()
    {
        setBackground(Color.WHITE);

        _timer = new Timer(1000 / ANIMATION_FPS, this);
    }

    //========================================================================= FUNCTIONS
    protected final void startAnimation()
    {
        startAnimation(DEFAULT_TAG);
    }
    protected final void startAnimation(String tag)
    {
        _aniProgresses.put(tag, ANIMATION_DURATION);
        _timer.start();
    }

    protected final void removeAnimation(String tag)
    {
        _aniProgresses.remove(tag);
    }

    protected final void removeAllAnimations()
    {
        _aniProgresses.clear();
    }

    //========================================================================= PROPERTIES
    protected final float getAniProgress()
    {
        return getAniProgress(DEFAULT_TAG);
    }
    protected final float getAniProgress(String tag)
    {
        Integer progress = _aniProgresses.get(tag);

        if (progress != null)
            return 1 - progress / (float)ANIMATION_DURATION;

        return 1;
    }

    //========================================================================= EVENTS
    public final void actionPerformed(ActionEvent e)
    {
        boolean isAnimationDone = true;

        for (String name : _aniProgresses.keySet())
        {
            int duration = _aniProgresses.get(name);

            if (duration > 1)
                isAnimationDone = false;

            if (duration > 0)
                _aniProgresses.put(name, duration - 1);
        }

        if (isAnimationDone)
            _timer.stop();

        onAniProgressChanged();
    }

    protected void onAniProgressChanged()
    {
    }
}
