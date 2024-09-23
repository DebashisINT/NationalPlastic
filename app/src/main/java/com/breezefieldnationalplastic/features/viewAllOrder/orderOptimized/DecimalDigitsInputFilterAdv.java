package com.breezefieldnationalplastic.features.viewAllOrder.orderOptimized;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalDigitsInputFilterAdv implements InputFilter
{
    Pattern pattern;

    public DecimalDigitsInputFilterAdv(int digitsBeforeDecimal, int digitsAfterDecimal)
    {
        pattern = Pattern.compile("(([1-9]{1}[0-9]{0," + (digitsBeforeDecimal - 1) + "})?||[0]{1})((\\.[0-9]{0," + digitsAfterDecimal + "})?)||(\\.)?");
    }

    @Override public CharSequence filter(CharSequence source, int sourceStart, int sourceEnd, Spanned destination, int destinationStart, int destinationEnd)
    {
        // Remove the string out of destination that is to be replaced.
        String newString = destination.toString().substring(0, destinationStart) + destination.toString().substring(destinationEnd, destination.toString().length());

        // Add the new string in.
        newString = newString.substring(0, destinationStart) + source.toString() + newString.substring(destinationStart, newString.length());

        // Now check if the new string is valid.
        Matcher matcher = pattern.matcher(newString);

        if(matcher.matches())
        {
            // Returning null indicates that the input is valid.
            return null;
        }

        // Returning the empty string indicates the input is invalid.
        return "";
    }
}
