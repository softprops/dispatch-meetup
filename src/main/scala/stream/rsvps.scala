package meetup.stream

import dispatch._

trait Rsvps { self: Client =>
  def rsvps = StreamBuilder(self, streamHost / "2" / "rsvps")
}
