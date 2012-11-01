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
     with Cities
     with Events
     with EventComments
     with Groups
     with Member
     with OpenEvents
     with Photos
     with Rsvps
     with Raw {
  def apply[T](req: RequestBuilder)(handler: Client.Handler[T]): Promise[T] =
    http(credentials.sign(req) > handler)
}
