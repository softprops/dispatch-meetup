package meetup.stream

import dispatch._
import net.liftweb.json.JValue

trait Photos { self: Client =>
  case class PhotosBuilder(eventVal: Option[String] = None,
                          sinceCountVal: Option[Int] = None,
                          sinceMtimeVal: Option[Long] = None)
       extends Client.Completion {

    def event(id: String) = copy(eventVal = Some(id))
    def sinceCount(n: Int) = copy(sinceCountVal = Some(n))
    def sinceMtime(t: Long) = copy(sinceMtimeVal = Some(t))

    def foreach[T](handler: Client.Handler[T]) =
      apply(streamHost / "2" / "photos" <<? pmap)(handler)

    private def pmap = Map.empty[String, String] ++ eventVal.map(
      "event_id" -> _
    ) ++ sinceCountVal.map(
      "since_count" -> _.toString
    ) ++ sinceMtimeVal.map(
      "since_mtime" -> _.toString
    )
  }

  def photos = PhotosBuilder()
}
