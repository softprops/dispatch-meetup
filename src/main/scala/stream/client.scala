package meetup.stream

import dispatch._
import dispatch.Defaults._
import com.ning.http.client.RequestBuilder
import org.json4s._
import org.json4s.native.JsonMethods._
import JsonDSL._

object Client {
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
    http(req > as.json4s.stream.Json(handler))
}
