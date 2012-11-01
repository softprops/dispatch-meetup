# Dispatch Meetup

A small [meetup.com](http://www.meetup.com/) client for dispatch

## General usage

### Authentication

The Meetup api supports multiple kinds of authentication. So does this client. The quickest way
to get off the ground is with your api key.

    import meetup._
    val client = meetup.rest.Client(Key("your_api_key"))

Once you have a client, you can start making requests. The philsophy of this client is to let you
decide what you want to use for deserializing API responses. The interface for doing so is by providing a function of `Response => T`.

    client.groups.urlname("ny-scala")(as.lift.Json)
    
The values returned are in the form of `dispatch.Promise`s which can be processed asynchronously until you call `apply()` on them.

## examples

    import meetup._
    import dispatch._
    import net.liftweb.json._

    object Main {
      def main(args: Array[String]) {
        // get the name of an organizer
        val cli = rest.Client(Key("API_KEY"))
        val org = for { 
          JObject(org) <- (cli.groups
                              .urlname("ny-scala")
                              .only("organizer")
                              .request(as.lift.Json)()) \\ "organizer"
          JField("name", JString(name)) <- org
        } yield name
        println(org)
        
        // get pushed a stream of all public rsvps
        meetup.stream.Client().rsvps.foreach(println)
      }
    }

