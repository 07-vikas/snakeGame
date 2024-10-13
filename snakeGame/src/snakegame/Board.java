package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {
    
    private int dots;
    private Image apple;
    private Image dot;
    private Image head;
    
    private final int ALL_DOTS = 1600;
    private final int DOT_SIZE = 10;
 
    private final int RANDOM_POSITION = 29;
    
    private int apple_x;
    private int apple_y;
    
    private Timer timer;
    
    private boolean inGame;
    
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    
    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];

    Board(){
        addKeyListener( new TAdapter());
        setBackground( Color.BLACK);
        setFocusable( true);
        loadImages();
        initGame();
    }
    
    public void loadImages( ){
        ImageIcon i1 = new ImageIcon( ClassLoader.getSystemResource( "snakegame/icons/apple.png"));
        apple = i1.getImage();        
        ImageIcon i2 = new ImageIcon( ClassLoader.getSystemResource("snakegame/icons/dot.png"));
        dot = i2.getImage();
        ImageIcon i3 = new ImageIcon( ClassLoader.getSystemResource("snakegame/icons/head.png"));
        head = i3.getImage();
        
    }
    
    public void initGame( ){
        dots = 3;
        
        inGame = true;
        
        for( int i = 0; i<dots; i++){
            y[i] = 50;
            x[i] = 50 - DOT_SIZE*i;
        }
        
        locateApple();
        timer = new Timer( 200, this);
        timer.start();
    }
    
    
    public void locateApple( ){
        int r = ( int)( Math.random() * RANDOM_POSITION);
        
        apple_x = r * DOT_SIZE;
        r = ( int)( Math.random() * RANDOM_POSITION);
        apple_y = r * DOT_SIZE;
    }
    
    @Override
    public void paintComponent( Graphics g){
        super.paintComponent( g);
        draw( g);
    }
    
    public void draw( Graphics g){
        if( inGame){
            g.drawImage(apple, apple_x, apple_y, this);
        
        for( int i = 0; i<dots; i++){
            if( i == 0){
                g.drawImage( head, x[i], y[i], this);
            }else{
                g.drawImage( dot, x[i], y[i], this);
            }
        }
        Toolkit.getDefaultToolkit().sync();
        }else{
            gameOver( g);
        }
    }
    
    public void gameOver( Graphics g){
        String str = "Game Over";
        
        Font font = new Font( "SAN_SERIF", Font.BOLD, 16);
        FontMetrics metrices = getFontMetrics( font);
        
        g.setColor( Color.RED);
        g.setFont( font);
        
        g.drawString( str, ( 400 - metrices.stringWidth( str))/2, 400/2);
    }
    
    public void move( ){
        for( int i = dots; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        
        if( leftDirection){
            x[0] = x[0] - DOT_SIZE;
        }
        if( rightDirection){
            x[0] = x[0] + DOT_SIZE;
        }
        if( upDirection){
            y[0] = y[0] - DOT_SIZE;
        }
        if( downDirection){
            y[0] = y[0] + DOT_SIZE;
        }
        
    }
    
    public void checkApple(){
        if( ( x[0] == apple_x) && ( y[0] == apple_y)){
            dots++;
            locateApple();
        }
    }
    
    public void checkCollision( ){
        
        for( int i = dots; i>0; i--){
            if( ( dots > 3) && ( x[0] == x[i]) && ( y[0] == y[i])){
                inGame = false;
            }
        }
        
        if( y[0] >= 400 || y[0] <= 0 || x[0] >= 400 || x[0] <= 0){
            inGame = false;
        }
        
        if( !inGame){
            timer.stop();
        }
    }
    
    public void actionPerformed( ActionEvent ae){
        if( inGame){
            checkApple();
            checkCollision();
            move();
        }
        
        repaint();
    }
    
    public class TAdapter extends KeyAdapter{
        public void keyPressed( KeyEvent e){
            int key = e.getKeyCode();
            if( key == KeyEvent.VK_LEFT && !rightDirection){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            
            if( key == KeyEvent.VK_RIGHT && !leftDirection){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            
            if( key == KeyEvent.VK_UP && !downDirection){
                leftDirection = false;
                rightDirection = false;
                upDirection = true;
            }
            
            if( key == KeyEvent.VK_DOWN && !upDirection){
                leftDirection = false;
                rightDirection = false;
                downDirection = true;
            }
            
            
        }
    }
}
