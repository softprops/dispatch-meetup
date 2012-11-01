package meetup.rest

import dispatch._

trait Rsvps { self: Client =>
  case class RsvpsBuilder(events: Option[Seq[String]] = None,
                          responseVal: Option[String] = None,
                          fieldextra: Option[Seq[String]] = None,
                          omits: Option[Seq[String]] = None,
                          onlys: Option[Seq[String]] = None)
       extends Client.Completion {

    def event(ids: String*) = copy(events = Some(ids))

    def response(r: String) = copy(responseVal = Some(r))

    def fields(fs: Seq[String]) = copy(fieldextra = Some(fs))

    def omit(fs: String*) = copy(omits = Some(fs))

    def only(fs: String*) = copy(onlys = Some(fs))

    override def request[T](handler: Client.Handler[T]) =
      apply(apiHost / "2" / "rsvps" <<? pmap)(handler)

    private def pmap = Map.empty[String, String] ++ events.map(
      "event_id" -> _.mkString(",")
    ) ++ responseVal.map(
      "rsvp" -> _
    ) ++ fieldextra.map(
      "fields" -> _.mkString(",")
    ) ++ omits.map(
      "omit" -> _.mkString(",")
    ) ++ onlys.map(
      "only" -> _.mkString(",")
    )
  }
  def rsvps = RsvpsBuilder()
}
