/*Task: Implement a Chat Application
 Develop a chat application using Java and
 networking libraries like Socket or Java NIO.
 This task will enhance their knowledge of
 network programming, client-server
 architecture, and message exchange.*/

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;

public class Server implements ActionListener {
    JTextField text;
    JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;

    Server() {
        // setup the frame
        f.setLayout(null);
        f.setSize(450, 700);
        f.setLocation(200, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.white);

        // Panel p1 for the header
        JPanel p1 = new JPanel();
        p1.setBackground(Color.GREEN);
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        // Back button
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/254427.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(5, 20, 25, 25);
        p1.add(back);
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                f.setVisible(false);

            }

        });

        // Additional icons
        addIcon(p1, "images/video.jpg", 300, 20, 35, 30);
        addIcon(p1, "images/call.png", 340, 20, 35, 30);
        addIcon(p1, "images/3151505.png", 380, 20, 35, 30);

        // Name and status labels
        JLabel name = new JLabel("John");
        name.setBounds(50, 15, 200, 15);
        name.setForeground(Color.black);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        p1.add(name);

        JLabel status = new JLabel("Active Now");
        status.setBounds(50, 30, 200, 15);
        status.setForeground(Color.black);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 14));
        p1.add(status);

        // Main panel for messages
        a1 = new JPanel();
        a1.setBounds(5, 75, 425, 570);
        a1.setLayout(new BorderLayout());
        f.add(a1);

        // Text field for input
        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(text);

        // Send button
        JButton send = new JButton("SEND");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(7, 94, 84));
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 18));
        f.add(send);

        f.setVisible(true);

    }

    private void addIcon(JPanel panel, String path, int x, int y, int width, int height) {
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(path));
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(img);
        JLabel label = new JLabel(scaledIcon);
        label.setBounds(x, y, width, height);
        panel.add(label);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String out = text.getText();
            JPanel p2 = formatLabel(out);
            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            a1.add(vertical, BorderLayout.PAGE_START);

            dout.writeUTF(out);

            text.setText(" ");

            f.revalidate();
            f.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style--\"width : 150 px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;

    }

    public static void main(String[] agrs) {
        new Server(); // create the GUI

        // start server in a separate thread to avoid blocking EDT
        new Thread(() -> {
            try {
                ServerSocket skt = new ServerSocket(5500);
                while (true) {
                    Socket s = skt.accept();
                    DataInputStream din = new DataInputStream(s.getInputStream());
                    dout = new DataOutputStream(s.getOutputStream());
                    while (true) {
                        String msg = din.readUTF();
                        JPanel panel = formatLabel(msg);
                        JPanel left = new JPanel(new BorderLayout());
                        left.add(panel, BorderLayout.LINE_START);
                        vertical.add(left);
                        f.revalidate();

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }
}
