import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 85;
    final int offerAfter=9;
    final int x[]=new int[GAME_UNITS];
    final int y[]=new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten=1;
    int appleX;
    int appleXOffer;
    int appleY;
    int appleYOffer;
    char direction = 'R';
    boolean running = false;
    boolean selected=false;
    boolean offerGiven=true;
    boolean selfAndBorderCollision=false;
    boolean selfCollision=false;
    boolean borderCollision=false;
    boolean noCollision=false;
    public static boolean again=false;
    Timer timer;
    Random random;


    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }


    public void startGame(){
        for(int i=bodyParts-1;i>=0;i--)
        {
            x[5-i]=(i+1)*UNIT_SIZE;
            y[i]=2*UNIT_SIZE;
        }
        newApple();
        running=true;
        timer=new Timer(DELAY,this);
        timer.start();


    }



    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }



    public void draw(Graphics g){

        /*this draws the gridlines row and columnwise
        for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
            g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
            g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
        }
        */


        if(selfAndBorderCollision || borderCollision)
        {
            g.setColor(Color.ORANGE);
            g.drawRect(5,5,SCREEN_WIDTH-5,SCREEN_HEIGHT-8);
            g.drawRect(UNIT_SIZE,UNIT_SIZE,SCREEN_WIDTH-2*UNIT_SIZE,SCREEN_HEIGHT-2*UNIT_SIZE);
        }
        if(selected)
        {
            if(applesEaten%offerAfter==0){
                g.setColor(Color.ORANGE);
                g.fillOval(appleXOffer,appleYOffer,2*UNIT_SIZE,2*UNIT_SIZE);
            }
            //A big orange color apple will be given which offers more points than the normal small red apples
            offerGiven=false;
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i=0;i<bodyParts;i++){
                if(i==0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    if(running) {
//                        g.setColor(new Color(45, 180, 0));
                        g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                    else{
                        if(x[i]==x[0] && y[i]==y[0]) {
                            g.setColor(Color.magenta);
                            g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                        }
                        else{
                            g.setColor(new Color(45, 180, 0));
                            g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                        }
                    }
                }

                if(running){
                    g.setColor(Color.white);
                    g.setFont(new Font("Ink Free",Font.BOLD,20));
                    FontMetrics metrics=getFontMetrics(g.getFont());
                    g.drawString("SCORE: " + (applesEaten-1)*10,(SCREEN_WIDTH-metrics.stringWidth("SCORE: " + (applesEaten-1)*10))/2,g.getFont().getSize());
                }

            }
            if(!running){
                gameOver(g);
            }
        }

        else{
            running=false;
            g.setColor(Color.green);
            g.setFont(new Font("Ink Free",Font.BOLD,40));
            FontMetrics metrics=getFontMetrics(g.getFont());
            g.drawString("Menu: ",(GamePanel.SCREEN_WIDTH-metrics.stringWidth("Menu: "))/2,g.getFont().getSize());

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD,25));
            FontMetrics metrics1=getFontMetrics(g.getFont());
            g.drawString("1. Border & Self Collision",(GamePanel.SCREEN_WIDTH-metrics1.stringWidth("1. Border & Self Collision"))/2, (GamePanel.SCREEN_HEIGHT/2)-(2*UNIT_SIZE));

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD,25));
            FontMetrics metrics2=getFontMetrics(g.getFont());
            g.drawString("2. Self Collision",(GamePanel.SCREEN_WIDTH-metrics2.stringWidth("2. Self Collision"))/2, (GamePanel.SCREEN_HEIGHT/2));

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD,25));
            FontMetrics metrics3=getFontMetrics(g.getFont());
            g.drawString("3. Border Collision",(GamePanel.SCREEN_WIDTH-metrics3.stringWidth("3. Border Collision  "))/2, (GamePanel.SCREEN_HEIGHT/2)+(2*GamePanel.UNIT_SIZE));

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD,25));
            FontMetrics metrics4=getFontMetrics(g.getFont());
            g.drawString("4. No collision",(GamePanel.SCREEN_WIDTH-metrics4.stringWidth("4. No collision"))/2, (GamePanel.SCREEN_HEIGHT/2)+(4*GamePanel.UNIT_SIZE));

        }

    }


    public void select(int n){
        selected=true;
        running=true;
        if(n==1){
            selfAndBorderCollision=true;
            selfCollision=false;
            borderCollision=false;
            noCollision=false;
        }
        else if(n==2){
            selfAndBorderCollision=false;
            selfCollision=true;
            borderCollision=false;
            noCollision=false;
        }
        else if(n==3){
            selfAndBorderCollision=false;
            selfCollision=false;
            borderCollision=true;
            noCollision=false;
        }
        else if(n==4){
            selfAndBorderCollision=false;
            selfCollision=false;
            borderCollision=false;
            noCollision=true;
        }
    }


    public void newApple(){
        appleX=UNIT_SIZE + random.nextInt((int)((SCREEN_WIDTH-2*UNIT_SIZE)/UNIT_SIZE))*UNIT_SIZE;
        appleXOffer=UNIT_SIZE + random.nextInt((int)((SCREEN_WIDTH-3*UNIT_SIZE)/UNIT_SIZE))*UNIT_SIZE;
        appleY=2*UNIT_SIZE+random.nextInt((int)((SCREEN_HEIGHT-3*UNIT_SIZE)/UNIT_SIZE))*UNIT_SIZE;
        appleYOffer=2*UNIT_SIZE+random.nextInt((int)((SCREEN_HEIGHT-4*UNIT_SIZE)/UNIT_SIZE))*UNIT_SIZE;
    }




    public void move(){
        for(int i=bodyParts;i>0;i--){
            x[i]=x[i-1];
            y[i]=y[i-1];
        }

        switch (direction){
                case 'U':
                y[0]=y[0]-UNIT_SIZE;
                break;
                case 'D':
                y[0]=y[0]+UNIT_SIZE;
                break;
                case 'L':
                x[0]=x[0]-UNIT_SIZE;
                break;
                case 'R':
                x[0]=x[0]+UNIT_SIZE;
                break;
        }
    }



    public void checkOffer(){
        if((x[0]==appleXOffer || x[0]==(appleXOffer+UNIT_SIZE)) && (y[0]==appleYOffer || y[0]==(appleYOffer+UNIT_SIZE))){
            applesEaten+=5;
            bodyParts++;
            //newApple();
        }
    }

    public void checkApple(){
//        if(offerGiven){
//            if((x[0]==appleXOffer || x[0]==(appleXOffer+UNIT_SIZE)) && (y[0]==appleYOffer || y[0]==(appleYOffer+UNIT_SIZE))){
//                applesEaten++;
//                bodyParts++;
//                newApple();
//            }
//        }
//        else {
            if (x[0] == appleX && y[0] == appleY) {
                applesEaten++;
                bodyParts++;
                newApple();
            }
//        }
    }




    public void checkCollisions(){

        if(selfAndBorderCollision){
            //this checks if head collides with body
            for(int i=bodyParts;i>0;i--)
            {
                if(x[0]== x[i] && y[0]==y[i]){
                    running=false;
                }
            }

            //this checks if body touches left border
            if(x[0]<UNIT_SIZE){
                running = false;
            }
            //this checks if body touches right border
            if(x[0]>=SCREEN_WIDTH-UNIT_SIZE){
                running = false;
            }
            //this checks if body touches top border
            if(y[0]<UNIT_SIZE){
                running = false;
            }
            //this checks if body touches bottom border
            if(y[0]>=SCREEN_HEIGHT-UNIT_SIZE){
                running = false;
            }
        }

        else if(selfCollision){
            //this checks if head collides with body
            for(int i=bodyParts;i>0;i--)
            {
                if(x[0]== x[i] && y[0]==y[i]){
                    running=false;
                }
            }

            //this checks if body touches left border
            if(x[0]<0){
                x[0]=SCREEN_WIDTH-UNIT_SIZE;
            }
            //this checks if body touches right border
            if(x[0]>=SCREEN_WIDTH){
                x[0]=0;
            }
            //this checks if body touches top border
            if(y[0]<0){
                y[0]=SCREEN_HEIGHT-UNIT_SIZE;
            }
            //this checks if body touches bottom border
            if(y[0]>=SCREEN_HEIGHT){
                y[0]=0;
            }
        }

        else if(borderCollision){
            //this checks if body touches left border
            if(x[0]<UNIT_SIZE){
                running = false;
            }
            //this checks if body touches right border
            if(x[0]>=SCREEN_WIDTH-UNIT_SIZE){
                running = false;
            }
            //this checks if body touches top border
            if(y[0]<UNIT_SIZE){
                running = false;
            }
            //this checks if body touches bottom border
            if(y[0]>=SCREEN_HEIGHT-UNIT_SIZE){
                running = false;
            }
        }

        else if(noCollision){
            //this checks if body touches left border
            if(x[0]<0){
                x[0]=SCREEN_WIDTH-UNIT_SIZE;
            }
            //this checks if body touches right border
            if(x[0]>=SCREEN_WIDTH){
                x[0]=0;
            }
            //this checks if body touches top border
            if(y[0]<0){
                y[0]=SCREEN_HEIGHT-UNIT_SIZE;
            }
            //this checks if body touches bottom border
            if(y[0]>=SCREEN_HEIGHT){
                y[0]=0;
            }
        }

        if(!running){
            timer.stop();
        }

    }




    public void gameOver(Graphics g){
        borderCollision=false;
        selfAndBorderCollision=false;
        g.setColor(Color.BLUE);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics=getFontMetrics(g.getFont());
        g.drawString("GAME OVER!!",(SCREEN_WIDTH-metrics.stringWidth("GAME OVER!!"))/2,SCREEN_HEIGHT/2);

        g.setColor(Color.PINK);
        g.setFont(new Font("Ink Free",Font.BOLD,30));
        FontMetrics metrics2=getFontMetrics(g.getFont());
        g.drawString("SCORE: " + (applesEaten-1)*10,(SCREEN_WIDTH-metrics2.stringWidth("SCORE: " + (applesEaten-1)*10))/2,g.getFont().getSize()+(SCREEN_HEIGHT/2)+UNIT_SIZE);

        g.setColor(Color.white);
        g.setFont(new Font("Ink Free",Font.BOLD,25));
        FontMetrics metrics1=getFontMetrics(g.getFont());
        g.drawString("Press Enter to Play Again",(SCREEN_WIDTH-metrics1.stringWidth("Press Enter to Play Again"))/2,(SCREEN_HEIGHT/2)+(4*UNIT_SIZE));
    }


    private void restart(){
        selected=false;
        running = true;
        applesEaten=1;
        bodyParts = 6;
        direction = 'R';
        for(int i=bodyParts-1;i>=0;i--)
        {
            x[5-i]=(i+1)*UNIT_SIZE;
            y[i]=2*UNIT_SIZE;
        }

        timer.start();
        newApple();
        repaint();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            if(applesEaten%offerAfter==0){
                checkOffer();
            }
            checkApple();
            checkCollisions();
        }
        repaint();
    }




    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    else{
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    else{
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    else{
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    else{
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    restart();
                    break;
                case KeyEvent.VK_1:
                    select(1);
                    break;
                case KeyEvent.VK_2:
                    select(2);
                    break;
                case KeyEvent.VK_3:
                    select(3);
                    break;
                case KeyEvent.VK_4:
                    select(4);
                    break;
            }

        }
    }



}
