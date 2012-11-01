package meetup.rest

import dispatch._
import com.ning.http.client.{ RequestBuilder, Response }

object Client {
  type Handler[T] = (Response => T)
  trait Completion {
    def request[T](handler: Client.Handler[T]): Promise[T]
  }
}

/** Meetup Rest API client */
case class Client(credentials: meetup.Credentials, http: Http = Http)
  extends meetup.DefaultHosts
     with Member
     with Groups
     with OpenEvents 
     with Rsvps
     with Cities {
  def apply[T](req: RequestBuilder)(handler: Client.Handler[T]): Promise[T] =
    http(credentials.sign(req) > handler)
}
