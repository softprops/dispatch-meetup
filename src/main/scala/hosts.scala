package meetup

import dispatch._
import com.ning.http.client.RequestBuilder

trait Hosts {
  def apiHost: RequestBuilder
  def streamHost: RequestBuilder
}

trait DefaultHosts extends Hosts {
  def apiHost = :/("api.meetup.com").secure
  def streamHost = :/("stream.meetup.com")
}
