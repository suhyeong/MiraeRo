package org.androidtown.miraero;

import android.content.Context;
import android.widget.ArrayAdapter;


public class QNASpinnerAdapter extends ArrayAdapter<String> {

    public QNASpinnerAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count-1 : count;
    }
}
