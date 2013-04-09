package org.jboss.resteasy.examples.service;

import com.wordnik.swagger.annotations.*;

import org.jboss.resteasy.examples.data.Book;
import org.jboss.resteasy.examples.data.BookListing;
import org.jboss.resteasy.annotations.providers.jaxb.json.BadgerFish;
import org.jboss.resteasy.plugins.providers.jaxb.json.BadgerContext;
import org.jboss.resteasy.plugins.providers.jaxb.json.JettisonMappedContext;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.xml.bind.Marshaller;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.StringWriter;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Api(value = "/library", 
   description = "the Library api")
@Path("/library.json")
public class Library
{
   private HashMap<String, Book> books = new HashMap<String, Book>();

   public Library()
   {
      books.put("596529260", new Book("Leonard Richardson", "596529260", "RESTful Web Services"));
      books.put("333333333", new Book("Bill Burke", "596529260", "EJB 3.0"));
   }

   @GET
   @Path("/books/badger")
   @Produces("application/json")
   @BadgerFish
   @ApiOperation(value = "gets books with Badger", notes = "gets books with @Badgerfish", responseClass = "org.jboss.resteasy.examples.data.BookListing")
   @ApiErrors(value = { @ApiError(code = 400, reason = "Not sure"), @ApiError(code = 404, reason = "bad") })
   public BookListing getBooksBadger()
   {
      return getListing();
   }

   @GET
   @Path("/books/mapped")
   @Produces("application/json")
   @ApiOperation(value = "gets books with mapped", notes = "gets books with @Mapped", responseClass = "org.jboss.resteasy.examples.data.BookListing")
   @ApiErrors(value = { @ApiError(code = 400, reason = "Not sure"), @ApiError(code = 404, reason = "bad") })
   //@Mapped // mapped is the default format
   public BookListing getBooksMapped()
   {
      return getListing();
   }

   @GET
   @Path("/books/badger.html")
   @Produces("text/html")
   public String getBooksBadgerText() throws Exception
   {
      BookListing listing = getListing();
      BadgerContext context = new BadgerContext(BookListing.class);
      StringWriter writer = new StringWriter();
      Marshaller marshaller = context.createMarshaller();
      marshaller.marshal(listing, writer);
      return writer.toString();
   }

   @GET
   @Path("/books/mapped.html")
   @Produces("text/html")
   public String getBooksMappedText() throws Exception
   {
      BookListing listing = getListing();
      JettisonMappedContext context = new JettisonMappedContext(BookListing.class);
      StringWriter writer = new StringWriter();
      Marshaller marshaller = context.createMarshaller();
      marshaller.marshal(listing, writer);
      return writer.toString();
   }

   private BookListing getListing()
   {
      ArrayList<Book> list = new ArrayList<Book>();
      list.addAll(books.values());
      return new BookListing(list);
   }
}
