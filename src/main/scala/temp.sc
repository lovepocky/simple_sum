import org.joda.time.DateTime
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.ext.JodaTimeSerializers

implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

import GlobalConfig._

val message = SumMessage("12345-1", 1, DateTime.now())

compact(render(Extraction.decompose(message)))
