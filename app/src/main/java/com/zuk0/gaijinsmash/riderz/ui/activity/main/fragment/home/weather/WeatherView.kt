package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.LifecycleObserver
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.weather_response.WeatherResponse
import org.w3c.dom.Attr

/*
   Custom View class that accepts WeatherResponse(JSON)
   and displays an appropriate image
   https://www.raywenderlich.com/142-android-custom-view-tutorial

   image assets should be white/transparent and filled dynamically.
 */
class WeatherView
constructor(context: Context?, attrs: AttributeSet): View(context, attrs), LifecycleObserver {

    companion object {
        private const val DEFAULT_SUN_COLOR = Color.YELLOW
        private const val DEFAULT_LIGHTNING_COLOR = Color.YELLOW
        private const val DEFAULT_CLOUDS_COLOR = Color.WHITE
        private const val DEFAULT_RAIN_COLOR = Color.BLUE
        private const val DEFAULT_SNOW_COLOR = Color.WHITE
        private const val DEFAULT_WIND_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_MOON_COLOR = Color.YELLOW

        const val SUNNY = 0
        const val WINDY = 1
        const val CLOUDY = 2
        const val RAINY = 3
        const val STORMY = 4
        const val SNOWY = 5
    }

    enum class WeatherState {
        SUNNY, CLOUDY, RAINY, STORMY, WINDY, FOGGY, SNOWY
    }

    //todo: tornadoes and hurricanes?
    // Paint object for coloring and styling
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    // Some colors for the face background, eyes and mouth.
    private var sunColor = DEFAULT_SUN_COLOR
    private var lightningColor = Color.YELLOW
    private var cloudsColor = Color.WHITE
    private var rainColor = Color.BLUE
    private var snowColor = Color.WHITE
    private var windColor = Color.WHITE
    private var skyColor = Color.BLUE
    private var borderColor = Color.GRAY
    private var moonColor = Color.YELLOW //TODO - get lunar cycle

    // weather object border width in pixels
    private var borderWidth = 4.0f
    // View size in pixels
    private var size = 320

    private lateinit var weatherResponse: WeatherResponse
    private lateinit var imageAsset: Drawable

    var weatherState = WeatherState.SUNNY
        set(state) {
            field = state
            invalidate()
        }

    init {
        paint.isAntiAlias = true
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        // Obtain a typed array of attributes

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        size = Math.min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }
    // TODO handle image assets at runtime with a image setter function


    override fun onDraw(canvas: Canvas?) {
        // call the super method to keep any drawing from the parent side.
        super.onDraw(canvas)
        //drawBackground(canvas)
    }

    private fun handleWeatherResponse() {
        //todo use when case to choose
    }

    /*
        This will create a simple overlay background
     */
    private fun drawBackground(canvas: Canvas?) {
        paint.color = skyColor
        paint.style = Paint.Style.FILL
        val radius = size / 2f

        canvas?.drawCircle(size / 2f, size / 2f, radius, paint)

        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth

        canvas?.drawCircle(size / 2f, size / 2f, radius - borderWidth / 2f, paint)
    }

    /*

     */
    private fun loadSnow() {

    }

    private fun loadRain() {

    }

    private fun loadCloud() {

    }

    private fun loadWind() {

    }

    private fun loadLightning() {

    }

    private fun loadThunder() {

    }

    private fun loadSun() {

    }

    private fun loadMoon() {

    }

    /*
        Lifecycle Events for view
     */
}