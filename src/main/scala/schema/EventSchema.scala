package schema

import org.joda.time.DateTime

/**
  * Created by lovepocky on 17/4/17.
  */
case class UserCreateEvent(id: String, community_id: String, create_at: DateTime)

case class UserLastSignInEvent(id: String, community_id: String, last_sign_in_at: DateTime)

case class UnknownEvent(raw: String)