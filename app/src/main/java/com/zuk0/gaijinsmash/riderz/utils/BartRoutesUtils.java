package com.zuk0.gaijinsmash.riderz.utils;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.utils.debug.DebugController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

public class BartRoutesUtils {

    private Context context;

    public BartRoutesUtils(Context context) {
        this.context = context;
    }

    public void setLineBarByRoute(String route, TextView coloredBar) {
        switch(route) {
            case "ROUTE 1": coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartYellowLine));
                break;
            case "ROUTE 2": coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartYellowLine));
                break;
            case "ROUTE 3": coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartOrangeLine));
                break;
            case "ROUTE 4": coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartOrangeLine));
                break;
            case "ROUTE 5": coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartGreenLine));
                break;
            case "ROUTE 6": coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartGreenLine));
                break;
            case "ROUTE 7": coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartRedLine));
                break;
            case "ROUTE 8":  coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartRedLine));
                break;
            case "ROUTE 11": coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartBlueLine));
                break;
            case "ROUTE 12": coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartBlueLine));
                break;
            case "ROUTE 19": coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartOakAirport));
                break;
            case "ROUTE 20": coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartOakAirport));
                break;
            default:        coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartDefault));
                break;
        }
    }

    public static void setLineBarByColor(Context context, String color, TextView line) {
        switch(color) {
            case "YELLOW": line.setBackgroundColor(context.getResources().getColor(R.color.bartYellowLine));
                break;
            case "GREEN": line.setBackgroundColor(context.getResources().getColor(R.color.bartGreenLine));
                break;
            case "RED": line.setBackgroundColor(context.getResources().getColor(R.color.bartRedLine));
                break;
            case "ORANGE": line.setBackgroundColor(context.getResources().getColor(R.color.bartOrangeLine));
                break;
            case "BLUE": line.setBackgroundColor(context.getResources().getColor(R.color.bartBlueLine));
                break;
            case "GRAY": line.setBackgroundColor(context.getResources().getColor(R.color.bartOakAirport));
                break;
            default: line.setBackgroundColor(context.getResources().getColor(R.color.bartDefault));
                break;
        }
    }

    public static String lineToColor(String line) {
        switch(line) {
            case "ROUTE 1": return "YELLOW";
            case "ROUTE 2": return "YELLOW";
            case "ROUTE 3": return "ORANGE";
            case "ROUTE 4": return "ORANGE";
            case "ROUTE 5": return "GREEN";
            case "ROUTE 6": return "GREEN";
            case "ROUTE 7": return "RED";
            case "ROUTE 8":  return "RED";
            case "ROUTE 11": return "BLUE";
            case "ROUTE 12": return "BLUE";
            case "ROUTE 19": return "GRAY";
            case "ROUTE 20": return "GRAY"; //todo: gray might no be the correct name
        }
        return "BLACK";
    }

}
