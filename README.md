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

    client.eventComments.event("123").request(as.lift.Json)
    
The values returned are in the form of `dispatch.Promise`s which can be processed asynchronously until you call `apply()` on them.

### Client types

This library packages both rest and stream clients. The rest client requires authentication. The stream client does not.

    val restClient = meetup.rest.Client(credentials)
    val streamClient = meetup.stream.Client()
    
The rest interface includes a fluent interface for most REST style API methods for building up requests. You can execute a request using the `request(handler)` method on any of these method interfaces. The handler function transforms the response. The `request` method itself returns a `Promise` value containing the computed result.

    val event = restClient.rsvps.event("123").request(as.lift.Json)
    
The stream interface includes a fluent interface for most stream API methods for building up requests. You can execute a request by using the `foreach(handler)` method on any of these method interfaces.

   streamClient.rsvps.foreach(handler)

The handler function will be called once for each streamed item.

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

