import org.joda.time.DateTime
import org.json4s._
import org.json4s.ext.DateTimeSerializer
import org.json4s.native.JsonMethods._

implicit val formats = DefaultFormats + DateTimeSerializer


case class time(a: DateTime)

val s = compact(render(Extraction.decompose(time(DateTime.now()))))

parse(s).extractOpt[time]