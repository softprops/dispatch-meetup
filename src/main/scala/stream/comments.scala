package meetup.stream

import dispatch._

trait EventComments { self: Client =>
  def eventComments = StreamBuilder(self, streamHost / "2" / "event_comments")
}
