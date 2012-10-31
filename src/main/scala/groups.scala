package meetup

import dispatch._

trait Groups { self: Client =>
  case class GroupsBuilder(urlnames: Option[Seq[String]] = None,
                           ids: Option[Seq[Int]] = None,
                           domains: Option[Seq[String]] = None,
                           fieldextra: Option[Seq[String]] = None,
                           omits: Option[Seq[String]] = None,
                           onlys: Option[Seq[String]] = None)
       extends Client.Completion {

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
