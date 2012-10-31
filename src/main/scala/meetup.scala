package meetup

import dispatch._
import com.ning.http.client.{
  RequestBuilder, Request, AsyncHandler, Response
}

object Client {
  type Handler[T] = (Response => T)
  trait Completion {
    def request[T](handler: Client.Handler[T]): Promise[T]
  }
}

/** Meetup Rest API client */
case class Client(credentials: Credentials, http: Http = Http)
  extends DefaultHosts
     with Member
     with Groups {
  def apply[T](req: RequestBuilder)(handler: Client.Handler[T]): Promise[T] =
    http(credentials.sign(req) > handler)
}

object Stream {
  import net.liftweb.json.JValue
  type Handler[T] = (JValue => T)
  trait Completion {
    def foreach[T](handler: Stream.Handler[T]): Promise[Unit]
  }
}

case class Stream(http: Http = Http) extends DefaultHosts {

  def apply[T](req: RequestBuilder)(handler: Stream.Handler[T]): Promise[Unit] =
    http(req > as.lift.stream.Json(handler))

  case class RsvpsBuilder(eventVal: Option[String] = None,
                          sinceCountVal: Option[Int] = None,
                          sinceMtimeVal: Option[Long] = None)
       extends Stream.Completion {

    def event(id: String) = copy(eventVal = Some(id))
    def sinceCount(n: Int) = copy(sinceCountVal = Some(n))
    def sinceMtime(t: Long) = copy(sinceMtimeVal = Some(t))

    def pmap = Map.empty[String, String] ++ eventVal.map(
      "event_id" -> _
    ) ++ sinceCountVal.map(
      "since_count" -> _.toString
    ) ++ sinceMtimeVal.map(
      "since_mtime" -> _.toString
    )
    def foreach[T](handler: Stream.Handler[T]) =
      apply(streamHost <<? pmap)(handler)
  }

  def rsvps = RsvpsBuilder()
}
