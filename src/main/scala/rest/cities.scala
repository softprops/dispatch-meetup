package meetup.rest

import dispatch._

trait Cities { self: Client =>
  case class CitiesBuilder(ll: Option[(Float, Float)] = None,
                           queryVal: Option[String] = None,
                           radiusVal: Option[Int] = None,
                           fieldextra: Option[Seq[String]] = None,
                           omits: Option[Seq[String]] = None,
                           onlys: Option[Seq[String]] = None)
       extends Client.Completion {

    def latlon(lat: Float, lon: Float) = copy(ll = Some((lat, lon)))

    def query(q: String) = copy(queryVal = Some(q))

    def radius(r: Int) = copy(radiusVal = Some(r))

    def fields(fs: Seq[String]) = copy(fieldextra = Some(fs))

    def omit(fs: String*) = copy(omits = Some(fs))

    def only(fs: String*) = copy(onlys = Some(fs))

    override def request[T](handler: Client.Handler[T]) =
      apply(apiHost / "2" / "groups" <<? pmap)(handler)

    private def pmap = Map.empty[String, String] ++ ll.map {
      case (lat, _) => ("lat" -> lat.toString)
    } ++ ll.map {
      case (_, lon) => ("lon" -> lon.toString)
    } ++ radiusVal.map(
      "radius" -> _.toString
    ) ++ queryVal.map(
      "query" -> _
    ) ++ fieldextra.map(
      "fields" -> _.mkString(",")
    ) ++ omits.map(
      "omit" -> _.mkString(",")
    ) ++ onlys.map(
      "only" -> _.mkString(",")
    )
  }

  def cities = CitiesBuilder()
}
