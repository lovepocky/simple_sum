package schema

/**
  * Created by lovepocky on 17/4/17.
  */
object ExtractUtility {

  def extract(input: (String, String)): Any = {
    import org.json4s._
    import org.json4s.native.JsonMethods._
    import org.json4s.ext.DateTimeSerializer
    implicit val formats = DefaultFormats + DateTimeSerializer

    val (key, value) = input
    val json = parse(value)
    val result = json.extractOpt[schema.UserCreateEvent]
      .orElse(json.extractOpt[schema.UserLastSignInEvent])
      .getOrElse(schema.UnknownEvent(value))
    //println(s"ExtractUtility.extract input: $input, result: $result")
    result
  }
}
