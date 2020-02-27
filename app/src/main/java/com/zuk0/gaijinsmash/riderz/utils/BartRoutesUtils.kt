package com.zuk0.gaijinsmash.riderz.utils

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.zuk0.gaijinsmash.riderz.R

object BartRoutesUtils {
    fun setLineBarByRoute(context: Context, route: String?, coloredBar: TextView) {
        when (route) {
            "ROUTE 1" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartYellowLine))
            "ROUTE 2" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartYellowLine))
            "ROUTE 3" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartOrangeLine))
            "ROUTE 4" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartOrangeLine))
            "ROUTE 5" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartGreenLine))
            "ROUTE 6" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartGreenLine))
            "ROUTE 7" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartRedLine))
            "ROUTE 8" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartRedLine))
            "ROUTE 9" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartBlueLine))
            "ROUTE 10" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartBlueLine))
            "ROUTE 11" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartBlueLine))
            "ROUTE 12" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartBlueLine))
            "ROUTE 13" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartPurpleLine))
            "ROUTE 14" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartPurpleLine))
            "ROUTE 19" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartOakAirport))
            "ROUTE 20" -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartOakAirport))
            else -> coloredBar.setBackgroundColor(context.resources.getColor(R.color.bartDefault))
        }
    }

    fun setLineBarByColor(context: Context, color: String?, line: View) {
        val colorStateList: ColorStateList?
        when (color) {
            "YELLOW" -> {
                colorStateList = ResourcesCompat.getColorStateList(context.resources, R.color.bartYellowLine, context.theme)
                //line.backgroundTintList(context.resources.getColor(R.color.bartYellowLine))
            }
            "GREEN" -> {
                colorStateList = ResourcesCompat.getColorStateList(context.resources, R.color.bartGreenLine, context.theme)

               // line.setBackgroundColor(context.resources.getColor(R.color.bartGreenLine))
            }
            "RED" -> {
                colorStateList = ResourcesCompat.getColorStateList(context.resources, R.color.bartRedLine, context.theme)

                //line.setBackgroundColor(context.resources.getColor(R.color.bartRedLine))
            }
            "ORANGE" -> {
                colorStateList = ResourcesCompat.getColorStateList(context.resources, R.color.bartOrangeLine, context.theme)

                //line.setBackgroundColor(context.resources.getColor(R.color.bartOrangeLine))
            }
            "BLUE" -> {
                colorStateList = ResourcesCompat.getColorStateList(context.resources, R.color.bartBlueLine, context.theme)

               // line.setBackgroundColor(context.resources.getColor(R.color.bartBlueLine))
            }
            "PURPLE" -> {
                colorStateList = ResourcesCompat.getColorStateList(context.resources, R.color.bartPurpleLine, context.theme)

              //  line.setBackgroundColor(context.resources.getColor(R.color.bartPurpleLine))
            }
            "GRAY" -> {
                colorStateList = ResourcesCompat.getColorStateList(context.resources, R.color.bartOakAirport, context.theme)

               // line.setBackgroundColor(context.resources.getColor(R.color.bartOakAirport))
            }
            "WHITE" -> {
                colorStateList = ResourcesCompat.getColorStateList(context.resources, R.color.bartDefault, context.theme)

               // line.setBackgroundColor(context.resources.getColor(R.color.bartDefault))
            }
            else -> {
                CrashLogUtil.log("Unknown train line : $color")
                colorStateList = ResourcesCompat.getColorStateList(context.resources, R.color.bartGreenLine, context.theme)

                //line.setBackgroundColor(context.resources.getColor(R.color.bartDefault))
            }
        }
        line.backgroundTintList = colorStateList
    }
}