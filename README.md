## dispatch Meetup

a small wip meetup client for dispatch

    import meetup._
    import dispatch._
    import net.liftweb.json._

    object Main {
      def main(args: Array[String]) {
        val cli = Client(Key("API_KEY"))
        val org = for { 
          JObject(org) <- (cli.groups
                              .urlname("ny-scala")
                              .only("organizer")
                              .request(as.lift.Json)()) \\ "organizer"
          JField("name", JString(name)) <- org
        } yield name
        println(org)
      }
    }

