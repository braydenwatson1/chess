
import server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(); // Create a Server object
        server.run(8080); // Start the server on port 8080

        System.out.println("Server is running on http://localhost:8080");
    }
}


