import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    int B_HEIGHT = 400;
    int B_WIDTH = 400;
    int MAX_DATS = 1600;
    int DOT_SIZE = 10;
    int DOTS;
    int x[] = new int[MAX_DATS];
    int y[] = new int[MAX_DATS];
    int apple_X;
    int apple_Y;
    Image body, head, apple;
    Timer timer;
    int DELAY = 200;
    boolean inGame = true;
    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;
    Board(){
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setBackground(Color.BLACK);
        initGame();
        loadImages();
    }
    //Initialize game
    public void initGame(){
        DOTS = 3;
        x[0] = 250;
        y[0] = 250;
        //Initialize Snakes Position
        for(int i=0; i<DOTS; i++){
            x[i] = x[0]+ (DOT_SIZE*i);
            y[i] = y[0];
        }
        //Initialize apple
        localApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }
    //Load image from resources folder to image object
    public void loadImages(){
        ImageIcon bodyIcon = new ImageIcon("src/resources/dot.png");
        body = bodyIcon.getImage();
        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();
        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
    }
    //Draw images at snakes and apple position
    @Override
    public void  paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }
    //draw image
    public void doDrawing(Graphics g){
        if(inGame){
            g.drawImage(apple, apple_X, apple_Y, this);
            for(int i=0; i<DOTS; i++){
                if(i==0){
                    g.drawImage(head, x[0], y[0], this);
                }
                else{
                    g.drawImage(body, x[i], y[i], this);
                }
            }
        }
        else{
            gameOver(g);
            timer.stop();
        }
    }
    //Randomize the apple position
    public void localApple(){
        apple_X = ((int)(Math.random()*39))*DOT_SIZE;
        apple_Y = ((int)(Math.random()*39))*DOT_SIZE;
    }
    //Check the collision with border and body;
    public void checkCollision(){
        //Collision with body
        for(int i=1; i<DOTS; i++){
            if(i>4 && x[0]==x[i] && y[0]==y[i]){
                inGame = false;
            }
        }
        //Collision with Border
        if(x[0]<0){
            inGame = false;
        }
        if(x[0]>=B_WIDTH){
            inGame = false;
        }
        if(y[0]<0){
            inGame = false;
        }
        if(y[0]>=B_HEIGHT){
            inGame = false;
        }
    }
    //Display Game Over message
    public void gameOver(Graphics g){
        String msg = "Game Over";
        int score = (DOTS-3)*100;
        String scoremsg = "Score: "+Integer.toString(score);
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fontMetrics = getFontMetrics(small);
        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH-fontMetrics.stringWidth(msg))/2, B_HEIGHT/4);
        g.drawString(scoremsg, (B_WIDTH-fontMetrics.stringWidth(scoremsg))/2, (3*B_HEIGHT)/4);
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent){
        if(inGame){
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }
    //Make Snake move
    public void move(){
        for(int i=DOTS-1; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(leftDirection){
            x[0] = x[0]-DOT_SIZE;
        }
        if(rightDirection){
            x[0] = x[0]+DOT_SIZE;
        }
        if(upDirection){
            y[0] = y[0]-DOT_SIZE;
        }
        if(downDirection){
            y[0] = y[0]+DOT_SIZE;
        }
    }
    //Make Snake eat food
    public void checkApple(){
        if(apple_X==x[0] && apple_Y==y[0]){
            DOTS++;
            localApple();
        }
    }
    //Implements Control
    private class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key = keyEvent.getKeyCode();
            if(key==keyEvent.VK_LEFT && !rightDirection){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key==keyEvent.VK_RIGHT && !leftDirection){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key==keyEvent.VK_UP && !downDirection){
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if(key==keyEvent.VK_DOWN && !upDirection){
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}
