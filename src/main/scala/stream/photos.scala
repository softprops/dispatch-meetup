package meetup.stream

import dispatch._
import net.liftweb.json.JValue

trait Photos { self: Client =>
  def photos = StreamBuilder(self, streamHost / "2" / "photos")
}
