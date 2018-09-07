import java.nio.channels.*;
import java.util.concurrent.ExecutorService;

public class ServerCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Object>
{
    private ExecutorService threadPool;
    private AsynchronousServerSocketChannel server;
    private Thread currentThread;

    public ServerCompletionHandler(AsynchronousServerSocketChannel server, ExecutorService pool, Thread thread)
    {
        this.server = server;
        threadPool = pool;
        currentThread  = thread;
    }

    public void completed(AsynchronousSocketChannel ch, Object att)
    {
       Server.handleConnection(ch);
       server.accept(att, this);
    }

    public void failed(Throwable e, Object att)
    {
        System.out.println(att + " - handler failed");
        e.printStackTrace();
    }
}