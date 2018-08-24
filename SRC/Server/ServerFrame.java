package Server;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/*
 * Frame���в��ֹ�����LayoutManager
 * ������FlowLayout BorderLayout GridLayout CardLayout GridBagLayout
 * FlowLayout ��ˮ����
 * BorderLayout�����ϱ������������Frame��Ĭ�ϲ��ֹ�����
 * GridLayout ��񲼾ֹ����������ձ�񻮷� new GridLayout(3,2);   3��2�� 
 * �÷���frame.setLayout(new FlowLayout(FlowLayout.LEFT));
 * frame.pack() �����Frame��С�İ��Ÿ�������Ĵ�С
 * ����������setLocation setSize setBounds ���ᱻ���ֹ����������ǣ����Ҫ�Լ����ô�С��Ӧ��ȡ�����ֹ�����setLayout(null)
 * 
 * �¼�����
 *  ���ڰ�ť��Ҫʵ��ActionListener����ӿڣ�����Ҳ��һ���࣬ʵ������actionPerformed()������������ActionListener����ƱȽϺ�
 *  ����һ���¼�����һ���������ʱ�����һ������myEvent()���ô���������һЩ
 *  ���ӿ�ʵ�����Լ��ķ��������ͱ��һ������
 
 */

public class ServerFrame extends Frame
{
        private static final int WIDTH = 600;
        private static final int HEIGHT = 400;
        private static final int FrameX = 400;
        private static final int FrameY = 300;
        Button b = new Button("�����ť���رշ�������");
        TextArea ta = new TextArea();
        Dialog confirmBox = new Dialog(this , true);            
        Button yes = new Button("��");
        Button no = new Button("��");
        Label info =new Label("ȷ��ֹͣ���з�������");
        Dialog frameClose = new Dialog(this ,"����ĳ���Ա", true);
        Label infoClose = new Label("               ɵ���𣿽���㰴ť�������û�ã�");
        
        public void launchFrame()
        {
            setBackground(Color.DARK_GRAY);
            setLocation(FrameX, FrameY);
            setSize(WIDTH , HEIGHT);
            setTitle("piChatServer");
            setLayout(null);
            setResizable(false);
            b.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent arg0)
                {
                    confirmBox.setVisible(true);
                }
                
            });
            addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                   frameClose.setVisible(true);
                }
            });
            ta.setEditable(false);
            add(b);
            b.setLocation(10, 30);
            b.setSize(WIDTH - 20, 45);
            add(ta);
            ta.setText("������������...\n");
            ta.setLocation(0, 80);
            ta.setSize(WIDTH, HEIGHT - 70);
           setVisible(true);
           confirmBox.setLayout(new FlowLayout());
           confirmBox.add(info );
           confirmBox.add(yes );
           confirmBox.add(no );
           confirmBox.setLocation(FrameX+150, FrameY + 170); 
           confirmBox.setSize(300, 150);
           frameClose.setLocation(FrameX+150, FrameY + 170); 
           frameClose.setSize(300, 150);
           frameClose.setLayout(new BorderLayout());
           frameClose.add(infoClose , BorderLayout.CENTER);
           myEvent();
        }
        
        //�¼�����
        private void myEvent()
        {
            frameClose.addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                  frameClose.setVisible(false);
                }
                
            });
            confirmBox.addWindowListener(new WindowAdapter() 
            {
                public void windowClosing(WindowEvent e)
                {
                        confirmBox.setVisible(false);
                }
                
            });
            yes.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent arg0)

                {
                    System.exit(0);
                }
            }   );
            no.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent arg0)

                {
                    confirmBox.setVisible(false);
                }
            }   );
           
        }
        
        public TextArea getTa()
        {
            return ta;
        }

  
}
