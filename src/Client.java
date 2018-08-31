import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Client
{
    public static void main (String [] args) throws InterruptedException
    {
        new Client().runClient();
    }

    private void runClient() throws  InterruptedException
    {
        final int port = 5002;
        try
        {
            AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", port);
            Future future = client.connect(hostAddress);
            future.get(); // returns null
            System.out.println("Client is started");
            Scanner stdin = new Scanner(System.in);
            String msg="";
            while (true)
            {
                System.out.print("Sending message to server: ");
                if (stdin.hasNextLine())
                    msg = stdin.nextLine();
                byte[] bytes = msg.getBytes();
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                Future result = client.write(buffer);
                System.out.println(new String(buffer.array()).trim());
                buffer.clear();
                if (new String(buffer.array()).trim().equals("Bye"))
                    break;
            }
            client.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
