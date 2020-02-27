package com.zuk0.gaijinsmash.riderz.utils.xml_parser

import com.zuk0.gaijinsmash.riderz.data.local.entity.bsa_response.Bsa
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

class BsaXmlParser(override var inputStream: InputStream) : BaseXmlParser<Bsa>(inputStream, "bsa") {

    override fun readObject(parser: XmlPullParser): Bsa? {
        parser.require(XmlPullParser.START_TAG, ns, "bsa")

        var description: String = ""
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when(parser.name) {
                "description" -> {
                    description = readDescription(parser)
                }
                else -> XmlParserUtil.skip(parser)
            }
        }
        val bsa = Bsa()
        bsa.description = description
        return bsa
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readDescription(parser: XmlPullParser) : String {
        parser.require(XmlPullParser.START_TAG, ns, "description")
        val description = XmlParserUtil.readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "description")
        return description
    }

    companion object {
        private const val TAG = "BsaXmlParser"
    }
}

