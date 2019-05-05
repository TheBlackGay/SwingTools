package ink.tiptop.client;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QrCodeMain {
    //创建窗体，JFrame
    JFrame f = new JFrame("二维码生成Text工具");
    JButton btn_choose_in = new JButton("二维码文件夹");
    JButton btn_choose_out = new JButton("输出的文件夹");

    JButton generate = new JButton("确认生成");

    //定义一个40列的单行文本框
    JTextField txt_in = new JTextField(40);
    JTextField txt_out = new JTextField(40);

    /*------------------下面是用于执行界面初始化的init方法-------------------------*/
    public void init() {
        txt_in.setText("请输入或者选择二维码保存的文件夹路径");
        txt_out.setText("请选择要输出的Text文件的位置");
//        CardLayout card = new CardLayout();
//        f.setLayout(card);
        //创建一个装载了文本框、按钮的JPanel
        // 主面板 需要放置到frame中
        JPanel panelMain = new JPanel();
        panelMain.add(txt_in);
        panelMain.add(btn_choose_in);
        panelMain.add(txt_out);
        panelMain.add(btn_choose_out);
        panelMain.add(generate);

        f.add(panelMain);//用BorderLayout布局管理器，放在最底层

        //创建一个装载了下拉选择框、三个JCheckBox的JPanel
        //设置关闭窗口时，退出程序
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
        //方法内部不再嵌套方法，而是调用语句了
        f.setLocationRelativeTo(null);
        f.setSize(600, 300);

        // --------------------添加事件------------------------
        // 按钮点击事件
        btn_choose_in.addMouseListener(new MyMouseAdapter("btn_choose_in"));
        btn_choose_out.addMouseListener(new MyMouseAdapter("btn_choose_out"));

        // 文本框点击事件
        txt_in.addMouseListener(new MyMouseAdapter("txt_in"));
        txt_out.addMouseListener(new MyMouseAdapter("txt_out"));

        // 生成text按钮
        generate.addActionListener(new MyActionListener("generate"));


    }

    // 鼠标事件
    class MyMouseAdapter extends MouseAdapter {
        // 判断是哪个对象
        private String which;

        public MyMouseAdapter(String which) {
            this.which = which;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println(which);
            if (which.startsWith("btn_")) {
                // 按钮
                JFileChooser fileChooser = new JFileChooser();
                FileSystemView fsv = FileSystemView.getFileSystemView(); //注意了，这里重要的一句
                fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
                fileChooser.setDialogTitle("请选择文件夹...");
                fileChooser.setApproveButtonText("确定");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(f);
                if (JFileChooser.APPROVE_OPTION == result) {
                    String path = fileChooser.getSelectedFile().getPath();
                    System.out.println("path: " + path);

                    if ("btn_choose_in".equals(which)) {
                        txt_in.setText(path);
                    } else if ("btn_choose_out".equals(which)) {
                        txt_out.setText(path);
                    }

                }
            } else if (which.startsWith("txt_")) {
                // 文本
                if (which.equals("txt_in")) {
                    txt_in.setText("");
                } else if (which.equals("txt_out")) {
                    txt_out.setText("");
                }
            } else {
                // 生成按钮
                // 不在此处
            }
        }
    }

    class MyActionListener implements ActionListener {

        private String which;

        public MyActionListener(String which) {
            this.which = which;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            String dirIn = txt_in.getText();
            String dirOut = txt_out.getText();

            String regex = "^[A-Za-z]:\\\\"; // 字母:\
            Pattern compile = Pattern.compile(regex);
            Matcher matcher1 = compile.matcher(dirIn);
            Matcher matcher2 = compile.matcher(dirOut);
            if (!matcher1.find() || !matcher2.find()) {
                JOptionPane.showMessageDialog(f, "请输入或者选择正确的文件夹路径");
                return;
            }
            // 获取该文件夹下面的所有 图片文件  text 为当前文件夹的路径
            File file = new File(dirIn);
            boolean isDir = file.isDirectory();
            if (!file.exists() || !isDir) {
                JOptionPane.showMessageDialog(f, "请选择正确的文件夹路径");
                return;
            }
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                JOptionPane.showMessageDialog(f, "文件夹为空,请选择有二维码图片的文件夹");
                return;
            }

            File outFilePath = new File(txt_out.getText());
            if (!outFilePath.exists()) {
                // 创建多级目录
                outFilePath.mkdirs();
            }
            // 根据时分秒生成文件名字
            String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".txt";
            String pathname = outFilePath + "\\" + fileName;
            File fileOutName = new File(pathname);
            if (!fileOutName.exists()) {
                FileOutputStream out = null;
                PrintStream p = null;
                try {
                    // 创建新文件
                    fileOutName.createNewFile();

                    System.out.println("输出位置为 : " + pathname);
                    out = new FileOutputStream(pathname);
                    p = new PrintStream(out);
                    int count = 0;
                    for (File file1 : files) {
                        String qrcodePath = dirIn + "\\" + file1.getName();
                        System.out.println(" qrcodePath ==  " + qrcodePath);
                        String content = null;
                        try {
                            content = PicUtil.decoderQRCode(qrcodePath);
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(f, "文件夹下包含了非法二维码图片:" + file1.getName());
                            continue;
                        }

                        if ("".endsWith(content)) {
                            continue;
                        }
                        p.println(content);
                        p.flush();
                        count++;
                    }
                    JOptionPane.showMessageDialog(f, "总共生成了" + count + "条正常数据");
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    try {
                        if (p != null) {
                            p.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}