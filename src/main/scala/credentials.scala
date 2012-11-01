package meetup

import dispatch._
import dispatch.oauth._
import com.ning.http.client.RequestBuilder
import com.ning.http.client.oauth._

sealed trait Credentials {
  def sign(req: RequestBuilder): RequestBuilder
}

case class Key(key: String) extends Credentials {
  def sign(req: RequestBuilder) =
    req <<? Map("key" -> key)
}

case class OAuth2(access: String) extends Credentials {
  def sign(req: RequestBuilder) =
    req <<? Map("access_token" -> access)
}


case class OAuth1(cons: ConsumerKey, token: RequestToken) extends Credentials {
  def sign(req: RequestBuilder) =
    req <@(cons, token)
}


trait MeetupConsumer extends oauth.SomeConsumer {
  def consumer: ConsumerKey
}

abstract class Auth(val callback: String) extends oauth.Exchange
  with oauth.SomeCallback 
  with oauth.SomeHttp
  with MeetupConsumer
  with MeetupEndpoints {
  val http = dispatch.Http
}

trait MeetupEndpoints extends oauth.SomeEndpoints {
  val requestToken = "https://api.meetup.com/oauth/request"
  val accessToken  = "https://api.meetup.com/oauth/access"
  val authorize    = "http://www.meetup.com/authorize"
}
