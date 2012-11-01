package meetup.stream

import dispatch._
import net.liftweb.json.JValue

trait EventComments { self: Client =>
  def eventComments = StreamBuilder(self, streamHost / "2" / "event_comments")
}
