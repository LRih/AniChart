package AniChart;

import java.awt.*;

public final class Colors
{
    private static final Color[] COLORS =
    {
        new Color(23, 118, 182),
        new Color(255, 127, 0),
        new Color(36, 162, 32),
        new Color(216, 36, 31),
        new Color(182, 23, 118),
        new Color(23, 182, 182),
        new Color(182, 182, 23),
        new Color(23, 118, 255),
        new Color(182, 118, 255),
        new Color(182, 118, 23),
        new Color(118, 118, 118)
    };

    public static Color get(int index)
    {
        return COLORS[index % COLORS.length];
    }
}
