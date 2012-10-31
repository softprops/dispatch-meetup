package meetup


import dispatch._
import com.ning.http.client.RequestBuilder

sealed trait Credentials {
  def sign(req: RequestBuilder): RequestBuilder
}

case class Key(key: String) extends Credentials {
  def sign(req: RequestBuilder) =
    req <<? Map("key" -> key)
}
