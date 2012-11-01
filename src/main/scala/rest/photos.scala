package meetup.rest

import dispatch._

trait Photos { self: Client =>
    case class PhotosBuilder(events: Option[Seq[String]] = None,
                             groupIds: Option[Seq[Int]] = None,
                             groupurls: Option[Seq[String]] = None,
                             members: Option[Seq[Int]] = None,
                             photos: Option[Seq[Int]] = None,
                             albums: Option[Seq[Int]] = None,
                             fieldextra: Option[Seq[String]] = None,
                             omits: Option[Seq[String]] = None,
                             onlys: Option[Seq[String]] = None)
       extends Client.Completion {

    def event(ids: String*) =
      copy(events = Some(ids))

    def group(ids: Int*) = copy(groupIds = Some(ids))

    def groupurl(urlnames: String*) = copy(groupurls = Some(urlnames))

    def member(ids: Int*) = copy(members = Some(ids))

    def id(ids: Int*) = copy(photos = Some(ids))

    def album(ids: Int*) = copy(albums = Some(ids))

    def fields(fs: Seq[String]) = copy(fieldextra = Some(fs))

    def omit(fs: String*) = copy(omits = Some(fs))

    def only(fs: String*) = copy(onlys = Some(fs))

    override def request[T](handler: Client.Handler[T]) =
      apply(apiHost / "2" / "photos" <<? pmap)(handler)

    private def pmap = Map.empty[String, String] ++ events.map(
      "event_id" -> _.mkString(",")
    ) ++ groupIds.map(
      "group_id" -> _.mkString(",")
    ) ++ groupurls.map(
      "group_urlname" -> _.mkString(",")
    ) ++ members.map(
      "member_id" -> _.mkString(",")
    ) ++ photos.map(
      "photo_id" -> _.mkString(",")
    ) ++ albums.map(
        "photo_album_id" -> _.mkString(",")
    ) ++ fieldextra.map(
      "fields" -> _.mkString(",")
    ) ++ omits.map(
      "omit" -> _.mkString(",")
    ) ++ onlys.map(
      "only" -> _.mkString(",")
    )
  }

  def photos = PhotosBuilder()
}
