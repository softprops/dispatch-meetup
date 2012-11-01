package meetup.stream

import dispatch._
import net.liftweb.json.JValue

trait OpenEvents { self: Client => 
  def openEvents = StreamBuilder(self, streamHost / "2" / "open_events")
}
