package AniChart;

import java.awt.*;

public final class Fonts
{
    public static final Font TITLE = new Font("Arial", Font.BOLD, 18);
    public static final Font AXIS = new Font("Arial", Font.BOLD, 14);
    public static final Font MINOR_AXIS = new Font("Arial", Font.PLAIN, 14);
    public static final Font TEXT = new Font("Arial", Font.PLAIN, 14);

    private Fonts()
    {
        throw new AssertionError();
    }
}
