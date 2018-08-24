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
 * Chat0.2 �����TextField��TextArea TextField�ǵ��������TextArea�Ƕ�������򣬵�����������ֻ����ʾ��
 * ������֮�䲼�ֲο�΢�ŵ��԰�
 *
 * Chat0.3 ��ӹرչ��ܣ�֮ǰ�Ѿ����ˣ��˰汾��ˮ��
 *
 * Chat0.4 �����TextField�ļ�����ActionListener�����ı�"���䡰,��������������䵽TextArea��ʾ������
 * TextField�������null
 *
 * Chat0.5 �����ChatServer�ˣ����ڵȴ��ͻ�����������
 *
 * Chat0.6 ChatClient�����connect������������ChatServer ������ַ��127.0.0.1
 *
 * Chat0.7 ��DataOutputStream��DataInputStream��Ϊ�ַ���������������String Serverֱ�Ӵ�ӡ����Ļ��
 *
 * Chat0.8 Ҫ����ı�������ͬ�����ж�Ҫ�õ��ı���һ�����óɳ�Ա�����Է������� 
 * Client ϵͳ���˳����ڹرմ��ڵ��¼������д���� Server
 * ��ѭ��Ƕ�ף�һ��Client���Ͼͽ����ڲ���ѭ�������ڲ��Ͻ����ַ��� ServerSocket��Accept���������ʽ������The method
 * blocks until connection is made�� 
 * 
 * Chat0.9
 * ��Ҫ��Server��Exception�Ĵ���readUTFҲ�����ʽ�������ȵ�����Ϊֹ�������û����Ѿ��رգ��ܵ��Ѿ��ϵ���
 * �޷������ַ����׳�IOException������������Exception������Ҳ�ѷ������Ĺܵ�(��)�رգ�Socket�ص���
 *
 * Chat0.9���� catch��ͬ��Exception���в��������������ʾ�򲹾Ȳ�����ϸ��Exception�ǽ����˸��Ӿ�ϸ�Ĵ���ʹ�����������
 * ��������Exception��ִ����catch Exception�е���䣩�������������ִ�С� ���ܣ�TextArea����Ϊֻ��������˻���append��
 * 
 *  Chat1.0
 *  ��̬����(main)���ܵ��÷Ǿ�̬������Ҫ���ھ�̬�����е��÷Ǿ�̬����
 * ��Ҫ���ⲿ����ʵ������ͨ��������÷������ڲ����ʹ�����ⲿ��Ԥ��Ƶľ�newʵ������
 * ���ܣ�������֧�ֶ���ͻ����ӣ�ÿһ���ͻ�һ���߳��������������첽�������
 * 
 * Chat1.1
 * �������˽���Ϣ���������ͻ���
 * 
 * Chat1.2
 * �ͻ������߳̽������Է������������û���Ϣ
 * tips���ڲ��������������棬����������������
 * �����쳣��ʽֹͣ�̡߳�
 * finally�е���䣬�����Ƿ���Exception����ȥִ�е�
 * ���� try �����������������쳣������finally �����Ǳ�֤Ҫִ�еġ�
 * ��� try ����������������ô�� try �����е���䶼ִ����֮����ִ�� finally ���顣
 * ��� try �����쳣������try�����з����쳣��������������Ͳ��ᱻִ���ˣ�Ӧ����ȥ��Ӧ�� catch �����쳣����Ȼ��ִ�� finally ���顣
 * ����finally����ϸ������https://www.ibm.com/developerworks/cn/java/j-lo-finally/index.html
 * 
 * Chat1.3
 * �������˰ѽ��ܿͻ�����ϢȺ���������ͻ�����
 * 
 * Chat1.4
 * Note: Closing a socket doesn't clear its connection state
 * ����EOFException����˵���Է������ˣ���ô��������Ӧ���߳�Ҳ��Ҫ����break
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
    
    //ִ���������������С����������
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
    
    //���ӷ�����
    private boolean connect()
    {
        try {
            client = new Socket(ADRESS, PORT);
            dos = new DataOutputStream(client.getOutputStream());
            dis = new DataInputStream(client.getInputStream());
            taContent.setText("���������ӳɹ������ڿ��Է�����Ϣ����");
            return true;
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
            return false;
        } catch (IOException e)
        {
            // e.printStackTrace();
            taContent.setText("ϵͳ��ʾ���޷����ӷ�������");
            return false;
        }
    }
    
    //�Ͽ�����
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
    
    // TextField�����ࣺ�ڲ���
    private class TFListener implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            // trim����ȥ�ո�
            String s = tfTxt.getText().trim();
            tfTxt.setText(null);
            taContent.append("\n��:\n" + s);

            try
            {
                if (dos != null)
                {
                    dos.writeUTF(s);
                    dos.flush();
                }
            }catch (SocketException e2)
                {
                    taContent.append("\n�������Ѿ��رգ��޷�������Ϣ��");
                }
            catch (IOException e1)
                {
                    e1.printStackTrace();
                }

        }

    }
    
    //�߳��ڲ��ࣺ���ڼ�����������
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
            //���ͻ����˳�ʱ���ִ������Ĵ����
            //��Exception����������ʽ�߳�ֹͣ���е㲻��
//System.out.println("byebye!");
        }
        catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
    }
    
    
    
}
