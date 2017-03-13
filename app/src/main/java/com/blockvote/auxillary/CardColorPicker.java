package com.blockvote.auxillary;

import com.blockvote.votingclient.R;

/**
 * Created by Beast Mode on 3/13/2017.
 */

public class CardColorPicker {
    private static final int[] colors = {R.color.card1Color, R.color.card2Color, R.color.card3Color};

    public static int NextColor(int i){
        return colors[i % colors.length];
    }
}
