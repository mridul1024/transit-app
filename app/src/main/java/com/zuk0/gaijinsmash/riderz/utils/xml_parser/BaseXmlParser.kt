package com.zuk0.gaijinsmash.riderz.utils.xml_parser

import android.util.Log
import android.util.Xml
import com.zuk0.gaijinsmash.riderz.BuildConfig
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.util.*

abstract class BaseXmlParser<T>
constructor(open var inputStream: InputStream,
            private val firstTagName: String) {

    @get:Throws(IOException::class, XmlPullParserException::class)
    val list: MutableList<T>
        get() {
            val results = parse(inputStream)
            inputStream.close()
            return results
        }

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(`is`: InputStream): MutableList<T> {
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
    private fun readFeed(parser: XmlPullParser): MutableList<T> {
        if (BuildConfig.DEBUG) Log.d("readFeed():", "***BEGINNING***")
        var list: List<T> = mutableListOf()
        parser.require(XmlPullParser.START_TAG, ns, "root")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            // Starts by looking for the first tag
            if (name == firstTagName) {
                list = readObjects(parser)
            } else {
                XmlParserUtil.skip(parser)
            }
        }
        return list as MutableList<T>
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun readObjects(parser: XmlPullParser): MutableList<T> {
        if (BuildConfig.DEBUG) Log.d("readObjects()", "***BEGINNING***")
        parser.require(XmlPullParser.START_TAG, ns, firstTagName)
        val list = mutableListOf<T>()
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            val name = parser.name
            if (name == "station") {
                readObject(parser)?.let { list.add(it) }
            } else {
                XmlParserUtil.skip(parser)
            }
        }
        return list
    }

    @Throws(XmlPullParserException::class, IOException::class)
    open fun readObject(parser: XmlPullParser): T? {
        //TODO - method override in child class
        return null
    }

    companion object {
        private const val TAG = "BaseXmlParser"
        internal val ns: String? = null

    }
}