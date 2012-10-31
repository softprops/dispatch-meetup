package meetup

import dispatch._
import com.ning.http.client.{
  RequestBuilder, Request, AsyncHandler, Response
}

object Client {
  type Handler[T] = (Response => T)
}

/** Meetup API client */
case class Client(credentials: Credentials, http: Http = Http)
  extends DefaultHosts
     with Member
     with Groups {
  def apply[T](req: RequestBuilder)(handler: Client.Handler[T]): Promise[T] =
    http(credentials.sign(req) > handler)
}

trait Hosts {
  def apiHost: RequestBuilder
}

trait DefaultHosts extends Hosts {
  def apiHost = :/("api.meetup.com").secure
}

trait Completion {
  def request[T](handler: Client.Handler[T]): Promise[T]
}
