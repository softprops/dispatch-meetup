package meetup

import dispatch._

trait Member { self: Client =>
  case class MemberBuilder(id: String,
                           fieldextra: Option[Seq[String]] = None,
                           omits: Option[Seq[String]] = None,
                           onlys: Option[Seq[String]] = None)
       extends Client.Completion {
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
