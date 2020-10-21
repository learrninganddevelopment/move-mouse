import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MoveMouse {
    public static final int FIVE_SECONDS = 5000;
    public static int MAX_Y = 400;
    public static int MAX_X = 400;
    public static boolean breakLoop = true;
    public static boolean terminate = false;
    public static String myButtonCurrentValue = "Start";
    public static Thread mouseMoveThread = new Thread();

    public static void main(String... args) throws Exception {
        JFrame newFrame = new JFrame("New Window");
        JButton myButton = new JButton(myButtonCurrentValue);
        myButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton currentButton = (JButton) (e.getSource());
                if (currentButton.getText().equalsIgnoreCase("Stop")) {
                    breakLoop = true;
                    myButtonCurrentValue = "Start";
                    myButton.setText("Start");
                } else {
                    breakLoop = false;
                    terminate = false;
                    myButtonCurrentValue = "Stop";
                    myButton.setText("Stop");
                    mouseMoveThread = new Thread() {
                        @Override
                        public void run() {
                            System.out.println("Starting thread.");
                                try {
                                    MoveMouse.moseMove();
                                } catch (AWTException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            System.out.println("Exiting thread.");
                        }
                    };
                    mouseMoveThread.start();
                }
            }
        });
        JButton myExitButton = new JButton("Exit");
        myExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                breakLoop = true;
                terminate = true;
                System.exit(0);
            }
        });
        newFrame.setLayout(new GridLayout(1,2));
        newFrame.add(myButton);
        newFrame.add(myExitButton);
        newFrame.pack();
        newFrame.setVisible(true);
    }

    public static void moseMove() throws AWTException, InterruptedException {
        Robot robot = new Robot();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("Height : " + screenSize.height);
        System.out.println("Width : " + screenSize.width);
        MAX_X = screenSize.width;
        MAX_Y = screenSize.height;
        Random random = new Random();

        while (!breakLoop) {
            int current_x = MouseInfo.getPointerInfo().getLocation().x;
            int current_y = MouseInfo.getPointerInfo().getLocation().y;
            int new_x = random.nextInt(MAX_X);
            int new_y = random.nextInt(MAX_Y);
            System.out.println("Current (x,y) : " + current_x +", " + current_y);
            System.out.println("new     (x,y) : " + new_x +", " + new_y);
            int noOfIterations = Math.abs(new_x - current_x);
            float slop = ((new_y - current_y) * 1.0f) / ((new_x - current_x) * 1.0f);
            System.out.println("Slop : " + slop);
            float c = current_y - (slop * current_y);
            System.out.println("Constant : " + c);
            for (int i = 1; i <noOfIterations; i++) {
                int temp_x = current_x + i;
                int temp_y = Math.round(slop * temp_x + c);
                System.out.println("temp x,y :" + temp_x + ", " + temp_y);

                //Thread.sleep(100);
                robot.mouseMove(temp_x, temp_y);
            }
            //robot.mouseMove(random.nextInt(MAX_X), random.nextInt(MAX_Y));
            //Thread.sleep(FIVE_SECONDS);
            //Thread.sleep(100);
            robot.mouseMove(new_x, new_y);
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
            Thread.sleep(2000);
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            System.out.println("final x,y :" + new_x + ", " + new_y);
            System.out.println("Movement complete");
            Thread.sleep(10000);
        }

    }
}
