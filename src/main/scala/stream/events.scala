package meetup.stream

import dispatch._

trait OpenEvents { self: Client => 
  def openEvents = StreamBuilder(self, streamHost / "2" / "open_events")
}
