package meetup.stream

import dispatch._

trait Photos { self: Client =>
  def photos = StreamBuilder(self, streamHost / "2" / "photos")
}
