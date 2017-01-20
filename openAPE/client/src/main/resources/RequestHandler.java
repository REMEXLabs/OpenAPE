import static spark.Spark.*;
public class RequestHandler {

get("/api/resources"/testResource", (req, res) -> userService.getAllUsers());

get("/api/listings/testListing", (req, res) -> userService.getAllUsers());

}
