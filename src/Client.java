import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client
{
    static AsynchronousSocketChannel client;
    static ExecutorService threadPool;
    public static void main (String [] args)
    {
        new Client().runClient();
    }

    private void runClient()
    {
        Thread currentThread;
        threadPool = Executors.newFixedThreadPool(2, Executors.defaultThreadFactory());
        final int port = 5007;

        try
        {
            currentThread = Thread.currentThread();
            client = AsynchronousSocketChannel.open();
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", port);
            client.connect(hostAddress,null, new ClientCompletionHandler());
            System.out.println("Client is started:");
            try
            {
                currentThread.join();
            }
            catch (InterruptedException ex)
            {
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void startListeners()
    {
        ClientMessages mess = new ClientMessages(client);
        MessageTask tas = new MessageTask(client);
        threadPool.execute(mess);
        threadPool.execute(tas);
    }
}
