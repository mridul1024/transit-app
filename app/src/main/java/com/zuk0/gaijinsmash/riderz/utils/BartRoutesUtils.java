package com.zuk0.gaijinsmash.riderz.utils;

import android.content.Context;
import android.widget.TextView;

import com.zuk0.gaijinsmash.riderz.R;
import com.zuk0.gaijinsmash.riderz.data.local.entity.trip_response.Leg;

import java.util.HashSet;
import java.util.List;

public class BartRoutesUtils {

    public static void setLineBarByRoute(Context context, String route, TextView coloredBar) {
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
            case "ROUTE 9":  coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartBlueLine));
                break;
            case "ROUTE 10":  coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartBlueLine));
                break;
            case "ROUTE 11": coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartBlueLine));
                break;
            case "ROUTE 12": coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartBlueLine));
                break;
            case "ROUTE 13": coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartPurpleLine));
                break;
            case "ROUTE 14": coloredBar.setBackgroundColor(context.getResources().getColor(R.color.bartPurpleLine));
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
            case "PURPLE": line.setBackgroundColor(context.getResources().getColor(R.color.bartPurpleLine ));
                break;
            case "GRAY": line.setBackgroundColor(context.getResources().getColor(R.color.bartOakAirport));
                break;
            case "WHITE": line.setBackgroundColor(context.getResources().getColor(R.color.bartDefault));
                //NOTE: White lines are for lines that do not have a specific line assignment yet
                break;
            default: line.setBackgroundColor(context.getResources().getColor(R.color.bartDefault));
                break;
        }
    }
}
