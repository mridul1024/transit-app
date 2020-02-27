package com.zuk0.gaijinsmash.riderz.utils.xml_parser

import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.Etd
import java.io.InputStream

class EtdXmlParser(inputStream: InputStream) : BaseXmlParser<Etd>(inputStream, "station") {

}