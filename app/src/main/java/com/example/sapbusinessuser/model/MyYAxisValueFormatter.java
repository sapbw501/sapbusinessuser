package com.example.sapbusinessuser.model;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class MyYAxisValueFormatter implements IAxisValueFormatter {


    private DecimalFormat mFormat;

    public MyYAxisValueFormatter() {
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return Math.round(value)+"";
    }

    /** this is only needed if numbers are returned, else return 0 */

    public int getDecimalDigits() { return 1; }
}
