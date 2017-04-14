/**
  * Created by lovepocky on 17/4/14.
  */
object DatabaseConnection {

  lazy val dummyPool: Unit = {
    val ip = java.net.InetAddress.getLocalHost.getHostAddress
    println(s"DummyPool created (${java.util.UUID.randomUUID().toString}): ip: $ip")
  }

}
