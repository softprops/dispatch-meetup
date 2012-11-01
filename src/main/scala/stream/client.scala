package meetup.stream

import dispatch._
import net.liftweb.json.JValue
import com.ning.http.client.RequestBuilder

object Client {
  import net.liftweb.json.JValue
  type Handler[T] = (JValue => T)
  trait Completion {
    def foreach[T](handler: Client.Handler[T]): Promise[Unit]
  }
}

case class Client(http: Http = Http)
     extends meetup.DefaultHosts
        with Rsvps
        with OpenEvents
        with EventComments
        with Photos {
  def apply[T](req: RequestBuilder)(handler: Client.Handler[T]): Promise[Unit] =
    http(req > as.lift.stream.Json(handler))
}
