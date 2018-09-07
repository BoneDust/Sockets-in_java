import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Client
{
    public static void main (String [] args)
    {
        new Client().runClient();
    }

    private void runClient()
    {
        final int port = 5002;
        try
        {
            AsynchronousSocketChannel clientSocket = AsynchronousSocketChannel.open();
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", port);
            Future future = clientSocket.connect(hostAddress);
            future.get(); // returns null
            System.out.println("Client is started:");
            Scanner stdin = new Scanner(System.in);
            while (true)
            {
                String message = sendMessage(clientSocket, stdin);
                if (message.equals("bye"))
                    break;
            }
            clientSocket.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private String sendMessage(AsynchronousSocketChannel socket, Scanner stdin)
    {
        System.out.print("Sending message to server: ");
        String msg = "";
        if (stdin.hasNextLine())
            msg = stdin.nextLine();
        else
            msg = "bye";
        byte[] bytes = msg.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        Future result = socket.write(buffer);
        System.out.println(new String(buffer.array()).trim());
        buffer.clear();
        return (msg);
    }
}
