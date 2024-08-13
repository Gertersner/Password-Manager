import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import java.awt.*;

class Main {

    public static void CreateLogIn(String user, String pass){
        //create a login at the first time iterating

        File f = new File("login.txt");
        if(!f.exists()){ //check if login page already exists
            try{
                FileWriter CreateLog = new FileWriter("login.txt", true);
                CreateLog.write(user + " " + pass);
                CreateLog.close();

            } catch (IOException e){
                System.out.println("read error");
                e.printStackTrace();
            }
        }
    }

    public static Boolean CheckLogIn(String user, String pass) throws IOException {
        //checks if the login information matches the recorded login information

        File input = new File("login.txt");
        BufferedReader reader = new BufferedReader(new FileReader(input));

        String check = (user + " " + pass);
        String currentL;
        boolean result = false;

        while((currentL = reader.readLine()) != null){
            String trimmL = currentL.trim();
            if(trimmL.equals(check)) {
                result = true;
            }else{
                result =  false;
            }

        }
        return result;
    }


    //add and remove method
    public static void addrem(int addremChoice, String user, String pass) throws IOException {
        //adding
        if (addremChoice == 1){
            try{
                FileWriter passDoc = new FileWriter("passwords.txt", true); //<- totally secure
                passDoc.write(user + " " + pass + "\n");
                passDoc.close();
            } catch (IOException e){
                System.out.println("read error");
                e.printStackTrace();
            }
            //removing
        }else if (addremChoice == 2){
            File input = new File("passwords.txt");
            File tempF = new File("tempFile.txt");

            BufferedReader reader = new BufferedReader(new FileReader(input));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempF));

            String removal = (user + " " + pass);
            String currentL;

            while((currentL = reader.readLine()) != null){
                String trimmL = currentL.trim();
                if(trimmL.equals(removal)) continue;
                writer.write(currentL + System.getProperty("line.separator"));
            }

            writer.close();
            reader.close();

            try {
                //copy to temp
                String temp;
                BufferedReader buffRead = new BufferedReader(new FileReader("passwords.txt"));
                BufferedWriter buffWrite = new BufferedWriter(new FileWriter("temp.txt"));
                while ((temp = buffRead.readLine()) != null) {
                    buffWrite.write(temp);
                    buffWrite.newLine();
                    buffWrite.flush();
                }
                //copy to passwords
                buffRead = new BufferedReader(new FileReader("tempFile.txt"));
                buffWrite = new BufferedWriter(new FileWriter("passwords.txt"));
                while ((temp = buffRead.readLine()) != null) {
                    buffWrite.write(temp);
                    buffWrite.newLine();
                    buffWrite.flush();
                }
                //copy to tempFile
                buffRead = new BufferedReader(new FileReader("temp.txt"));
                buffWrite = new BufferedWriter(new FileWriter("tempFile.txt"));
                while ((temp = buffRead.readLine()) != null) {
                    buffWrite.write(temp);
                    buffWrite.newLine();
                    buffWrite.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //searches for a specified password and returns if it was found or not
    public static void search (String user) throws IOException
    {
        boolean found = false;
        try
        {
            FileReader passReader = new FileReader("passwords.txt");
            BufferedReader bufferedReader = new BufferedReader(passReader);

            String newLine = "";
            while((newLine = bufferedReader.readLine()) != null)
            {
                //splits the line b/w website and password
                String[] s = newLine.split(" ");
                //set check to the string after the space
                String pass = s[s.length-1];

                //return "The password " +pass+ " was found";
                if (user.equals(s[0])){
                    JOptionPane.showMessageDialog(null, "Vendor: " + user + "\nPassword: " + pass);
                    found = true;
                    break;
                }
            }
            passReader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if(!found){
            JOptionPane.showMessageDialog(null, "There is no password for the vendor " + user + ". \n Please make sure you typed the vendor in correctly.");
        }
    }

    public static void main(String[] args) throws IOException {
        GUI.main();
    }
}

class GUI implements ActionListener {

    public static void logInGUI(){
        //create frame
        JFrame frame = new JFrame("Passlock");
        frame.setSize(750, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //create components
        JLabel label1 = new JLabel(" Please enter your username and password:");
        JLabel label1dot2 = new JLabel(" "); //this is a jank solution to having one textbox take up the entire first row
        JLabel label2 = new JLabel(" Username:");
        JLabel label3 = new JLabel(" Password:");

        JTextField txt1 = new JTextField();
        txt1.setBounds(50,50,150,40);
        JTextField txt2 = new JTextField();
        txt2.setBounds(50,50,150,40);
        JButton btn1 = new JButton(new AbstractAction("Confirm") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = txt1.getText();
                String pass = txt2.getText();
                try {
                    if(Main.CheckLogIn(user, pass)){
                        GUI.mainGUI();
                        frame.dispose();
                    }else{
                        JOptionPane.showMessageDialog(null, "Invalid login");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        JButton btn2 = new JButton(new AbstractAction("Create New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                File f = new File("login.txt");
                if(!f.exists()){
                    CreateLogGUI();
                }else{
                    JOptionPane.showMessageDialog(null, "Account was already created");
                }

            }
        });


        //create panel and hold components
        JPanel panel = new JPanel(new GridLayout(4, 2, 50, 10));

        panel.add(label1);
        panel.add(label1dot2);
        panel.add(label2);
        panel.add(txt1);
        panel.add(label3);
        panel.add(txt2);
        panel.add(btn2);
        panel.add(btn1);

        // build GUI
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void CreateLogGUI(){
        //create frame
        JFrame frame = new JFrame("Passlock");
        frame.setSize(750, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //create components
        JLabel label1 = new JLabel(" Enter your new username and password:");
        JLabel label1dot2 = new JLabel(" ");
        JLabel label2 = new JLabel(" Username:");
        JLabel label3 = new JLabel(" Password:");

        JTextField txt1 = new JTextField();
        txt1.setBounds(50,50,150,40);
        JTextField txt2 = new JTextField();
        txt2.setBounds(50,50,150,40);
        JButton btn1 = new JButton(new AbstractAction("Confirm") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = txt1.getText();
                String pass = txt2.getText();
                Main.CreateLogIn(user, pass);
                frame.dispose();
            }
        });



        //create panel and hold components
        JPanel panel = new JPanel(new GridLayout(4, 2, 50, 10));

        panel.add(label1);
        panel.add(label1dot2);
        panel.add(label2);
        panel.add(txt1);
        panel.add(label3);
        panel.add(txt2);
        panel.add(btn1);

        // build GUI
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void mainGUI()
    {
        // create frame
        JFrame frame = new JFrame("PassLock");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create components
        JButton btn1 = new JButton(new AbstractAction("Add/Remove Passwords") {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.addremGUI();
            }
        });

        JButton btn2 = new JButton(new AbstractAction("View Passwords") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    GUI.displayGUI();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        JButton btn3 = new JButton(new AbstractAction("Search Passwords") {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.searchGUI();
            }
        });
        JButton btn4 = new JButton(new AbstractAction("Exit Passlock") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // create panel and hold components
        JPanel panel = new JPanel(new GridLayout(4, 1, 50, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);
        panel.add(btn4);

        // build GUI
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.setResizable(false);
    }
    public static void displayGUI() throws IOException {
        // create frame
        JFrame frame = new JFrame("PassLock");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create panel
        JPanel panel = new JPanel(new GridLayout(10, 1, 50, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));

        // create components and hold components
        JButton btn1 = new JButton(new AbstractAction("Back") {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        panel.add(btn1);

        try
        {
            FileReader passReader = new FileReader("passwords.txt");
            BufferedReader bufferedReader = new BufferedReader(passReader);
            String newLine = "";

            while((newLine = bufferedReader.readLine()) != null)
            {
                JLabel label = new JLabel(newLine);
                panel.add(label);
            }
            passReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        // build GUI
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
    public static void addremGUI() {
        // create frame
        JFrame frame = new JFrame("PassLock");
        frame.setSize(500, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create components
        JButton btn1 = new JButton(new AbstractAction("Add Password") {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterpasswordGUI(1);
            }
        });

        JButton btn2 = new JButton(new AbstractAction("Remove Password") {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterpasswordGUI(2);
            }
        });

        JButton btn3 = new JButton(new AbstractAction("Back") {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        // create panel and hold components
        JPanel panel = new JPanel(new GridLayout(3, 1, 50, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);

        // build GUI
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
    public static void enterpasswordGUI(int choice){
        //create frame
        JFrame frame = new JFrame("Passlock");
        frame.setSize(750, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //create components
        JLabel label1 = new JLabel(" Please enter your username and password:");
        JLabel label1dot2 = new JLabel(" "); //this is a jank solution to having one textbox take up the entire first row
        JLabel label2 = new JLabel(" Website:");
        JLabel label3 = new JLabel(" Password:");

        JTextField txt1 = new JTextField();
        txt1.setBounds(50,50,150,40);

        JTextField txt2 = new JTextField();
        txt2.setBounds(50,50,150,40);

        JButton btn1 = new JButton(new AbstractAction("Confirm") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = txt1.getText();
                String pass = txt2.getText();
                try {
                    switch(choice){
                        case 1,2:
                            Main.addrem(choice, user, pass);
                            frame.dispose();
                            break;
                        case 3:
                            Main.search(user);
                            frame.dispose();
                            break;
                    }

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //create panel and hold components
        JPanel panel = new JPanel(new GridLayout(4, 2, 50, 10));

        panel.add(label1);
        panel.add(label1dot2);
        panel.add(label2);
        panel.add(txt1);
        panel.add(label3);
        panel.add(txt2);
        panel.add(btn1);

        // build GUI
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.setResizable(false);
    }
    public static void searchGUI(){
        //create frame
        JFrame frame = new JFrame("Passlock");
        frame.setSize(750, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //create components
        JLabel label1 = new JLabel(" Please enter the vendor for the password you are seeking");
        JLabel label1dot2 = new JLabel(" "); //this is a jank solution to having one textbox take up the entire first row
        JLabel label2 = new JLabel(" Website:");


        JTextField txt1 = new JTextField();
        txt1.setBounds(50,50,150,40);

        JTextField txt2 = new JTextField();
        txt2.setBounds(50,50,150,40);

        JButton btn1 = new JButton(new AbstractAction("Confirm") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = txt1.getText();
                try {
                    Main.search(user);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                frame.dispose();

                }
            });
        JButton btn2 = new JButton(new AbstractAction("Back") {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });


        //create panel and hold components
        JPanel panel = new JPanel(new GridLayout(4, 2, 50, 10));

        panel.add(label1);
        panel.add(label1dot2);
        panel.add(label2);
        panel.add(txt1);
        panel.add(btn2);
        panel.add(btn1);

        // build GUI
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    //DON'T REMOVE ANYTHING BELOW THIS LINE
    //this is load-bearing spaghetti
    @Override
    public void actionPerformed(ActionEvent e) {
        //this is just here so the code works
    }
    public static void main() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                logInGUI();
                // mainGUI();
            }
        });
    }
}