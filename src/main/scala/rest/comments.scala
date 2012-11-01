package meetup.rest

import dispatch._

trait EventComments { self: Client =>
    case class EventCommentsBuilder(events: Option[Seq[String]] = None,
                                    comments: Option[Seq[String]] = None,
                                    groups: Option[Seq[Int]] = None,
                                    members: Option[Seq[Int]] = None,
                                    showDiffs: Option[Boolean] = None,
                                    fieldextra: Option[Seq[String]] = None,
                                    omits: Option[Seq[String]] = None,
                                    onlys: Option[Seq[String]] = None)
       extends Client.Completion {

    def event(ids: String*) = copy(events = Some(ids))

    def group(ids: Int*) = copy(groups = Some(ids))

    def member(ids: Int*) = copy(members = Some(ids))

    def diffs(d: Boolean) = copy(showDiffs = Some(d))

    def fields(fs: Seq[String]) = copy(fieldextra = Some(fs))

    def omit(fs: String*) = copy(omits = Some(fs))

    def only(fs: String*) = copy(onlys = Some(fs))

    override def request[T](handler: Client.Handler[T]) =
      apply(apiHost / "2" / "event_comments" <<? pmap)(handler)

    private def pmap = Map.empty[String, String] ++ events.map(
      "event_id" -> _.mkString(",")
    ) ++ groups.map(
      "group_id" -> _.mkString(",")
    ) ++ comments.map(
      "comment_id" -> _.mkString(",")
    ) ++ members.map(
      "member_id" -> _.mkString(",")
    ) ++ showDiffs.map(
      "show_diffs" -> _.toString
    ) ++ fieldextra.map(
      "fields" -> _.mkString(",")
    ) ++ omits.map(
      "omit" -> _.mkString(",")
    ) ++ onlys.map(
      "only" -> _.mkString(",")
    )
  }

  def eventComments = EventCommentsBuilder()

}
