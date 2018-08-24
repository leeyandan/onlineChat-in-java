package Client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.*;
import java.io.*;

/**
 * Chat0.2 添加了TextField和TextArea TextField是单行输入框，TextArea是多行输入框，但是我们用它只是显示框
 * 各个框之间布局参考微信电脑版
 *
 * Chat0.3 添加关闭功能，之前已经加了，此版本划水了
 *
 * Chat0.4 添加了TextField的监听器ActionListener用于文本"运输“,将输入的文字运输到TextArea显示，并将
 * TextField设置清空null
 *
 * Chat0.5 添加了ChatServer端，用于等待客户端连接上来
 *
 * Chat0.6 ChatClient添加了connect方法连接上了ChatServer 本机地址是127.0.0.1
 *
 * Chat0.7 用DataOutputStream和DataInputStream作为字符流连接起来传输String Server直接打印在屏幕上
 *
 * Chat0.8 要保存的变量，不同方法中都要用到的变量一般设置成成员变量以方便引用 
 * Client 系统的退出是在关闭窗口的事件处理中处理的 Server
 * 死循环嵌套，一旦Client连上就进入内层死循环，用于不断接受字符流 ServerSocket的Accept方法是阻断式方法（The method
 * blocks until connection is made） 
 * 
 * Chat0.9
 * 主要是Server端Exception的处理，readUTF也是阻断式方法。等到读到为止，但是用户端已经关闭，管道已经断掉了
 * 无法读得字符而抛出IOException，所有我们在Exception处理中也把服务器的管道(流)关闭，Socket关掉。
 *
 * Chat0.9补丁 catch不同的Exception进行操作，例如输出提示或补救操作，细分Exception是进行了更加精细的处理，使程序更加明了
 * 当处理完Exception（执行完catch Exception中的语句），程序继续向下执行。 功能：TextArea设置为只读，添加了换行append。
 * 
 *  Chat1.0
 *  静态方法(main)不能调用非静态方法，要想在静态方法中调用非静态方法
 * 需要把外部对象实例化，通过对象调用方法。内部类的使用在外部类预设计的就new实例化了
 * 功能：服务器支持多个客户连接，每一个客户一个线程在倾听。但是异步处理更好
 * 
 * Chat1.1
 * 服务器端将信息发往其他客户端
 * 
 * Chat1.2
 * 客户端起线程接受来自服务器的其他用户信息
 * tips：内部类排在类最下面，方法排在它的上面
 * 用抛异常方式停止线程。
 * finally中的语句，不论是否有Exception都会去执行的
 * 不管 try 语句块正常结束还是异常结束，finally 语句块是保证要执行的。
 * 如果 try 语句块正常结束，那么在 try 语句块中的语句都执行完之后，再执行 finally 语句块。
 * 如果 try 语句块异常结束，try语句块中发生异常那条语句后面的语句就不会被执行了，应该先去相应的 catch 块做异常处理，然后执行 finally 语句块。
 * 关于finally的详细描述：https://www.ibm.com/developerworks/cn/java/j-lo-finally/index.html
 * 
 * Chat1.3
 * 服务器端把接受客户的消息群发到各个客户端了
 * 
 * Chat1.4
 * Note: Closing a socket doesn't clear its connection state
 * 出现EOFException就是说明对方下线了，那么服务器对应的线程也就要死掉break
 */

public class ChatClient extends Frame
{

    public static final int WIDTH = 835;
    public static final int HEIGHT = 634;
    public static final String ADRESS = "127.0.0.1";
    public static final int PORT = 5500;

    TextField tfTxt = new TextField();
    TextArea taContent = new TextArea();
    Socket client = null;
    DataOutputStream dos = null;
    DataInputStream dis = null;
    private boolean bConnected = false;
    
    public static void main(String[] args)
    {
        new ChatClient().launchFrame();

    }
    
    //执行型主方法：大框小框设计在这儿
    public void launchFrame()
    {
        setLocation(534, 207);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        addWindowListener(new WindowAdapter()
        {

            public void windowClosing(WindowEvent e)
            {
                disconnect();
                System.exit(0);
            }
        });
        //setLayout(new GridLayout(2, 1));
        add(taContent , BorderLayout.NORTH);
        add(tfTxt , BorderLayout.SOUTH);
        pack();
        setResizable(false);
        setTitle("piChatClient");
        taContent.setEditable(false);
        //taContent.setBackground(Color.gray);
        tfTxt.addActionListener(new TFListener());
        if(connect())
            {
                bConnected = true;
                new Thread(new ReceiveClient()).start();
            }
    }
    
    //连接服务器
    private boolean connect()
    {
        try {
            client = new Socket(ADRESS, PORT);
            dos = new DataOutputStream(client.getOutputStream());
            dis = new DataInputStream(client.getInputStream());
            taContent.setText("服务器连接成功！现在可以发送消息啦！");
            return true;
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
            return false;
        } catch (IOException e)
        {
            // e.printStackTrace();
            taContent.setText("系统提示：无法连接服务器！");
            return false;
        }
    }
    
    //断开连接
    public void disconnect()
    {
        try
        {
            if (dos != null)
                dos.close();
            if (dis != null)
                dis.close();
            if(client != null)
                client.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    // TextField监听类：内部类
    private class TFListener implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            // trim用于去空格
            String s = tfTxt.getText().trim();
            tfTxt.setText(null);
            taContent.append("\n我:\n" + s);

            try
            {
                if (dos != null)
                {
                    dos.writeUTF(s);
                    dos.flush();
                }
            }catch (SocketException e2)
                {
                    taContent.append("\n服务器已经关闭！无法发送消息！");
                }
            catch (IOException e1)
                {
                    e1.printStackTrace();
                }

        }

    }
    
    //线程内部类：用于监听接受数据
    private class ReceiveClient implements Runnable
    {
        public void run()
        {
        try{
            while(bConnected)
            {
                String message = dis.readUTF();
                //System.out.println(message);
                taContent.append("\n"+message);
            }
            
        }catch (SocketException e)
        {
            //当客户端退出时候会执行这里的代码块
            //用Exception来处理阻塞式线程停止，有点不妥
//System.out.println("byebye!");
        }
        catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
    }
    
    
    
}
