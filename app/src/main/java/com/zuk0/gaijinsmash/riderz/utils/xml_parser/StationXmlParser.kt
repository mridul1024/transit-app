package com.zuk0.gaijinsmash.riderz.utils.xml_parser

import android.util.Log
import android.util.Xml
import com.zuk0.gaijinsmash.riderz.BuildConfig
import com.zuk0.gaijinsmash.riderz.data.local.entity.station_response.Station
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.util.*

class StationXmlParser(private val mInputStream: InputStream) {
    @get:Throws(IOException::class, XmlPullParserException::class)
    val list: List<Station>
        get() {
            val results = parse(mInputStream)
            mInputStream.close()
            return results
        }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun parse(`is`: InputStream): List<Station> {
        if (BuildConfig.DEBUG) Log.d("parse()", "***BEGINNING***")
        return try {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(`is`, null)
            parser.nextTag()
            readFeed(parser)
        } finally {
            `is`.close()
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser): List<Station> {
        if (BuildConfig.DEBUG) Log.d("readFeed():", "***BEGINNING***")
        var stationList: List<Station> = ArrayList()
        parser.require(XmlPullParser.START_TAG, ns, "root")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            // Starts by looking for the first tag
            if (name == "stations") {
                stationList = readStations(parser)
            } else {
                XmlParserUtil.skip(parser)
            }
        }
        return stationList
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readStations(parser: XmlPullParser): List<Station> {
        if (BuildConfig.DEBUG) Log.d("readStations()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "stations")
        val stationList: MutableList<Station> = ArrayList()
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            if (name == "station") {
                stationList.add(readStationObject(parser))
            } else {
                XmlParserUtil.skip(parser)
            }
        }
        return stationList
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readStationObject(parser: XmlPullParser): Station {
        parser.require(XmlPullParser.START_TAG, ns, "station")
        var mName: String? = null
        var mAbbreviation: String? = null
        var mLatitude: String? = null
        var mLongitude: String? = null
        var mAddress: String? = null
        var mCity: String? = null
        var mCounty: String? = null
        var mState: String? = null
        var mZipcode: String? = null
        var mPlatformInfo: String? = null
        var mIntro: String? = null
        var mCrossStreet: String? = null
        var mFood: String? = null
        var mShopping: String? = null
        var mAttraction: String? = null
        var mLink: String? = null
        // todo: change to switch case
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            when (name) {
                "name" -> {
                    mName = readName(parser)
                    if (BuildConfig.DEBUG) Log.i("mName", mName)
                }
                "abbr" -> {
                    mAbbreviation = readAbbr(parser)
                    if (BuildConfig.DEBUG) Log.i("abbreviation", mAbbreviation)
                }
                "gtfs_latitude" -> {
                    mLatitude = readLatitude(parser)
                    if (BuildConfig.DEBUG) Log.i("latitude", mLatitude)
                }
                "gtfs_longitude" -> {
                    mLongitude = readLongitude(parser)
                    if (BuildConfig.DEBUG) Log.i("longitude", mLongitude)
                }
                "address" -> {
                    mAddress = readAddress(parser)
                    if (BuildConfig.DEBUG) Log.i("address", mAddress)
                }
                "city" -> {
                    mCity = readCity(parser)
                    if (BuildConfig.DEBUG) Log.i("city", mCity)
                }
                "county" -> {
                    mCounty = readCounty(parser)
                    if (BuildConfig.DEBUG) Log.i("county", mCounty)
                }
                "state" -> {
                    mState = readState(parser)
                    if (BuildConfig.DEBUG) Log.i("state", mState)
                }
                "zipcode" -> {
                    mZipcode = readZipcode(parser)
                    if (BuildConfig.DEBUG) Log.i("zipcode", mZipcode)
                }
                "platform_info" -> mPlatformInfo = readPlatformInfo(parser)
                "intro" -> mIntro = readIntro(parser)
                "cross_street" -> mCrossStreet = readCrossStreet(parser)
                "food" -> mFood = readFood(parser)
                "shopping" -> mShopping = readShopping(parser)
                "attraction" -> mAttraction = readAttraction(parser)
                "link" -> mLink = readLink(parser)
                else -> XmlParserUtil.skip(parser)
            }
        }
        val station = Station()
        station.name = mName!!
        station.abbr = mAbbreviation
        station.latitude = mLatitude!!.toDouble()
        station.longitude = mLongitude!!.toDouble()
        station.address = mAddress
        station.city = mCity
        station.county = mCounty
        station.state = mState
        station.zipcode = mZipcode
        station.platformInfo = mPlatformInfo
        station.intro = mIntro
        station.attraction = mAttraction
        station.crossStreet = mCrossStreet
        station.shopping = mShopping
        station.food = mFood
        station.link = mLink
        return station
    }

    //----------------------------------------------------------------------------------------------
// Processes name tags in the StationInfo feed
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readName(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readName()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "name")
        val name = XmlParserUtil.readText(parser)
        // ADD REGEX to modify "international"
//name.replace("International", "Int'l");
        parser.require(XmlPullParser.END_TAG, ns, "name")
        return name
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readAddress(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readAddress()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "address")
        val address = XmlParserUtil.readText(parser)
        //todo address.replaceAll("International", "Int'l");
        parser.require(XmlPullParser.END_TAG, ns, "address")
        return address
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readLatitude(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readLatitude()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "gtfs_latitude")
        val latitude = XmlParserUtil.readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "gtfs_latitude")
        return latitude
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readLongitude(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readLongitude()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "gtfs_longitude")
        val longitude = XmlParserUtil.readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "gtfs_longitude")
        return longitude
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readAbbr(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readAbbr()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "abbr")
        val abbr = XmlParserUtil.readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "abbr")
        return abbr
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readCity(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readCity()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "city")
        val city = XmlParserUtil.readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "city")
        return city
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readCounty(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readCounty()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "county")
        val county = XmlParserUtil.readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "county")
        return county
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readState(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readState()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "state")
        val state = XmlParserUtil.readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "state")
        return state
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readZipcode(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readZipcode()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "zipcode")
        val zipcode = XmlParserUtil.readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "zipcode")
        return zipcode
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readPlatformInfo(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readPlatformInfo()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "platform_info")
        val platformInfo = XmlParserUtil.readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "platform_info")
        return platformInfo
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readIntro(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readIntro()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "intro")
        val intro = XmlParserUtil.readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "intro")
        return intro
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readCrossStreet(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readCrossStreet()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "cross_street")
        val crossStreet = XmlParserUtil.readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "cross_street")
        return crossStreet
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readFood(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readFood()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "food")
        val food = XmlParserUtil.readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "food")
        return food
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readShopping(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readShopping()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "shopping")
        val shopping = XmlParserUtil.readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "shopping")
        return shopping
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readAttraction(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readAttraction()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "attraction")
        val attraction = XmlParserUtil.readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "attraction")
        return attraction
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readLink(parser: XmlPullParser): String {
        if (BuildConfig.DEBUG) Log.i("readLink()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, "link")
        val link = XmlParserUtil.readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "link")
        return link
    }

    companion object {
        // require(int type, String namespace, String name) if namespace is null, will pass when matched against any name
        private val ns: String? = null
    }

}