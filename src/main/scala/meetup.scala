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

trait Groups { self: Client =>
  case class GroupsBuilder(urlnames: Option[Seq[String]] = None,
                           ids: Option[Seq[Int]] = None,
                           domains: Option[Seq[String]] = None,
                           fieldextra: Option[Seq[String]] = None,
                           omits: Option[Seq[String]] = None,
                           onlys: Option[Seq[String]] = None)
       extends Completion {

    def urlname(ns: String*) = copy(urlnames = Some(ns))
    def id(ids: Int*) = copy(ids = Some(ids))
    def domains(ds: String*) = copy(domains = Some(ds))
     def fields(fs: Seq[String]) = copy(fieldextra = Some(fs))
    def omit(fs: String*) = copy(omits = Some(fs))
    def only(fs: String*) = copy(onlys = Some(fs))
    override def request[T](handler: Client.Handler[T]) =
      apply(apiHost / "2" / "groups" <<? pmap)(handler)
    private def pmap = Map.empty[String, String] ++ urlnames.map(
      "group_urlname" -> _.mkString(",")
    ) ++ ids.map(
      "group_id" -> _.mkString(",")
    ) ++ domains.map(
      "domain" -> _.mkString(",")
    ) ++ fieldextra.map(
      "fields" -> _.mkString(",")
    ) ++ omits.map(
      "omit" -> _.mkString(",")
    ) ++ onlys.map(
      "only" -> _.mkString(",")
    )
  }
  def groups = GroupsBuilder()
}

trait Member { self: Client =>
  case class MemberBuilder(id: String,
                           fieldextra: Option[Seq[String]] = None,
                           omits: Option[Seq[String]] = None,
                           onlys: Option[Seq[String]] = None)
       extends Completion {
    def fields(fs: Seq[String]) = copy(fieldextra = Some(fs))
    def omit(fs: String*) = copy(omits = Some(fs))
    def only(fs: String*) = copy(onlys = Some(fs))

    private def pmap = Map.empty[String, String] ++ fieldextra.map(
      "fields" -> _.mkString(",")
    ) ++ omits.map(
      "omit" -> _.mkString(",")
    ) ++ onlys.map(
      "only" -> _.mkString(",")
    )

    override def request[T](handler: Client.Handler[T]) =
      self.apply((self.apiHost / "2" / "member" / id) <<? pmap)(handler)
  }
  def member(id: String) = MemberBuilder(id)
}
