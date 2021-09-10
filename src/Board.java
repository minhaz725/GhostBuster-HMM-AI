import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Random;

public class Board {
    int M;
    int N;
    double [][]prob_matrix;
    int [][]dis_matrix;
    int ghost_x;
    int ghost_y;
    int near;
    int medium;
    int far;
    int transition_prob_lin;
    int transition_prob_dign;
    int sensed_x;
    int sensed_y;

    public Board(int m, int n) {
        M = m;
        N = n;
        near = 2;
        medium = 5;
        prob_matrix = new double[M][N];
        dis_matrix = new int[M][N];
        initializeProbMatrix();
        initializeGhostPos();
    }

    public int manhattanDistance(int x, int y){
        return (Math.abs(ghost_x-x) + Math.abs(ghost_y-y));
    }

    public int manhattanDistance2Point(int x, int y,int a, int b){
        return (Math.abs(a-x) + Math.abs(b-y));
    }

    public void updateDistanceMatrix(){
        for(int i=0;i<M;i++){
            for(int j=0;j<N;j++){
                dis_matrix[i][j] = manhattanDistance(i,j);
            }
        }
    }
    public double getPosVal(int x,int y){
        return (x <0 || x >= M || y <0 || y >=N)? 0.0 : prob_matrix[x][y];
    }
    public double getCellProb(int i,int j){
        double sum = 0.0;
        //System.out.print(getLinProb(i,j)+" ");
        sum+= (getPosVal(i,j+1)*getLinProb(i,j+1)  +getPosVal(i,j-1)*getLinProb(i,j-1) +getPosVal(i-1,j)*getLinProb(i-1,j)+getPosVal(i+1,j)*getLinProb(i+1,j));
        sum+= (getPosVal(i-1,j+1)*getDiagonalProb(i-1,j+1) +getPosVal(i-1,j-1)*getDiagonalProb(i-1,j-1) +getPosVal(i+1,j-1)*getDiagonalProb(i+1,j-1) +getPosVal(i+1,j+1)*getDiagonalProb(i+1,j+1) +getPosVal(i,j)*getDiagonalProb(i,j));
        return sum;
    }
    public void normalizeProbMatrix(){
        double sum=0.0,factor;
        for(int i=0;i<M;i++){
            for(int j=0;j<N;j++){
                sum+= prob_matrix[i][j];
            }
        }
        factor = 100.0/sum;
        for(int i=0;i<M;i++){
            for(int j=0;j<N;j++){
                prob_matrix[i][j]*= factor;
            }
        }
    }

    public void updateProbMatrix(){
        double [][]temp = new double[M][N];
        for(int i=0;i<M;i++) {
            for (int j = 0; j < N; j++) {
                temp[i][j] = prob_matrix[i][j];
            }
        }
        for(int i=0;i<M;i++) {
            for (int j = 0; j < N; j++) {
                temp[i][j] = getCellProb(i,j);
            }
            //System.out.println();
        }
        for(int i=0;i<M;i++) {
            for (int j = 0; j < N; j++) {
                prob_matrix[i][j] =  temp[i][j];
            }
        }
        //normalizeProbMatrix();
    }

    public double getLinProb(int x,int y){
        if (x <0 || x >= M || y <0 || y >=N) return 0.0; //outside board
        setTransitionProb(x,y);
        return 0.96/transition_prob_lin;
    }
    public double getDiagonalProb(int x,int y){
        if (x <0 || x >= M || y <0 || y >=N) return 0.0; //outside board
        setTransitionProb(x,y);
        return 0.04/transition_prob_dign;
    }
    public void setTransitionProb(int x,int y){
        if((x==0 || x==M-1)&&(y==0 || y==N-1)) { //corner cell
            transition_prob_lin = 2;
            transition_prob_dign = 2;
        }
        else if(x>0 && x<M-1 && y>0 && y<N-1) {  //medium cell
            transition_prob_lin = 4;
            transition_prob_dign = 5;
        }
        else {                                  //side edge cell
            transition_prob_lin = 3;
            transition_prob_dign = 3;
        }
    }
    public void moveGhost(){
        Random rand = new Random();
        double prob = rand.nextDouble();
        setTransitionProb(ghost_x,ghost_y);
        double linProb = 0.96/transition_prob_lin;
        double diagProb = 0.04/transition_prob_dign;
        if(ghost_x==0 && ghost_y==0){
            if(prob < linProb) ghost_y++;
            else if(prob < 2*linProb) ghost_x++;
            else if(prob < 0.96+diagProb) {
                ghost_x++;
                ghost_y++;
            }
        }
        else if(ghost_x==0 && ghost_y==N-1){
            if(prob < linProb) ghost_y--;
            else if(prob < 2*linProb) ghost_x++;
            else if(prob < 0.96+diagProb) {
                ghost_x++;
                ghost_y--;
            }
        }
        else if(ghost_x==M-1 && ghost_y==0){
            if(prob < linProb) ghost_y++;
            else if(prob < 2*linProb) ghost_x--;
            else if(prob < 0.96+diagProb) {
                ghost_x--;
                ghost_y++;
            }
        }
        else if(ghost_x==M-1 && ghost_y==N-1){
            if(prob < linProb) ghost_y--;
            else if(prob < 2*linProb) ghost_x--;
            else if(prob < 0.96+diagProb) {
                ghost_x--;
                ghost_y--;
            }
        }
        else if(ghost_x==0){ //first row
            if(prob < linProb) ghost_y--;
            else if(prob < 2*linProb) ghost_x++;
            else if(prob < 3*linProb) ghost_y++;
            else if(prob < 0.96+diagProb) {
                ghost_x++;
                ghost_y--;
            }
            else if(prob < 0.96+2*diagProb) {
                ghost_x++;
                ghost_y++;
            }
        }
        else if(ghost_x==M-1){ //last row
            if(prob < linProb) ghost_y--;
            else if(prob < 2*linProb) ghost_x--;
            else if(prob < 3*linProb) ghost_y++;
            else if(prob < 0.96+diagProb) {
                ghost_x--;
                ghost_y--;
            }
            else if(prob < 0.96+2*diagProb) {
                ghost_x--;
                ghost_y++;
            }
        }
        else if(ghost_y==0){ //first column
            if(prob < linProb) ghost_y++;
            else if(prob < 2*linProb) ghost_x--;
            else if(prob < 3*linProb) ghost_x++;
            else if(prob < 0.96+diagProb) {
                ghost_x++;
                ghost_y++;
            }
            else if(prob < 0.96+2*diagProb) {
                ghost_x--;
                ghost_y++;
            }
        }
        else if(ghost_y==N-1){ //last column
            if(prob < linProb) ghost_y--;
            else if(prob < 2*linProb) ghost_x--;
            else if(prob < 3*linProb) ghost_x++;
            else if(prob < 0.96+diagProb) {
                ghost_x--;
                ghost_y--;
            }
            else if(prob < 0.96+2*diagProb) {
                ghost_x++;
                ghost_y--;
            }
        }
        else { //mid position
            if(prob < linProb) ghost_x--;
            else if(prob < 2*linProb) ghost_x++;
            else if(prob < 3*linProb) ghost_y--;
            else if(prob < 4*linProb) ghost_y++;
            else if(prob < 0.96+diagProb) {
                ghost_x++;
                ghost_y++;
            }
            else if(prob < 0.96+2*diagProb) {
                ghost_x++;
                ghost_y--;
            }
            else if(prob < 0.96+3*diagProb) {
                ghost_x--;
                ghost_y++;
            }
            else if(prob < 0.96+4*diagProb) {
                ghost_x--;
                ghost_y--;
            }
        }

     }

    public void initializeGhostPos(){
        Random rand = new Random();
        ghost_x = rand.nextInt(9);
        ghost_y = rand.nextInt(9);
    }

    public void initializeProbMatrix(){
        double p = 1.0/(M*N)*100.0;
        for(int i=0;i<M;i++){
            for(int j=0;j<N;j++){
                prob_matrix[i][j] = p;
            }
        }
    }

    public void printProbMatrix(){
        double p = 1.0/(M*N)*100.0;
        for(int i=0;i<M;i++){
            for(int j=0;j<N;j++){
                System.out.print(new DecimalFormat("##0.00").format(prob_matrix[i][j])+" ");
            }
            System.out.println();
        }
    }

    public int senseCellAction(){
        int dist = manhattanDistance(sensed_x,sensed_y);
        int color;
        if(dist <= near) color = 0;
        else if(dist <= medium) color = 1;
        else color = 2;

        if(color == 0){
            for(int i=0;i<M;i++){
                for(int j=0;j<N;j++){
                    if(manhattanDistance2Point(i,j,sensed_x,sensed_y) <= near) prob_matrix[i][j]*= 1.0;
                    else prob_matrix[i][j]*= 0.0;
                }
            }
        }
        else if(color == 1){
            for(int i=0;i<M;i++){
                for(int j=0;j<N;j++){
                    if((manhattanDistance2Point(i,j,sensed_x,sensed_y) > near) && (manhattanDistance2Point(i,j,sensed_x,sensed_y) <= medium)) prob_matrix[i][j]*= 1.0;
                    else prob_matrix[i][j]*= 0.0;
                }
            }
        }
        else {
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    if (manhattanDistance2Point(i, j, sensed_x, sensed_y) > medium) prob_matrix[i][j] *= 1.0;
                    else prob_matrix[i][j] *= 0.0;
                }
            }
        }
        normalizeProbMatrix();
        return color;
    }
    public boolean catchAction(){
        if(sensed_x==ghost_x && sensed_y==ghost_y) {
            for(int i=0;i<M;i++){
                for(int j=0;j<N;j++){
                    prob_matrix[i][j]=0.0;
                }
            }
            return true;
        }
        prob_matrix[sensed_x][sensed_y] = 0.0;
        normalizeProbMatrix();
        return false;

    }
    public void change(JButton[][] buttonGrid,JFrame frame,JPanel containerPanel, JPanel buttonPanel){
        frame.getContentPane().remove(containerPanel);
        containerPanel.remove(buttonPanel);
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new GridLayout(M, N));
        buttonPanel2.setPreferredSize(new Dimension(800, 600));

        ActionListener buttonListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                //System.out.println("GridButton Listener");
                JButton selectedBtn = (JButton) evt.getSource();
                for (int row = 0; row < buttonGrid.length; row++) {
                    for (int col = 0; col < buttonGrid[row].length; col++) {
                        if (buttonGrid[row][col] == selectedBtn) {
                            sensed_x = row;
                            sensed_y = col;
                            System.out.printf("Selected row and column: %d %d%n", row, col);
                        }
                    }
                }//end of for loop
                change(buttonGrid,frame,containerPanel,buttonPanel2);
            }
        };
        if(Main.catchFlag==false) {
            int color = senseCellAction();
            for(int i=0;i<M;i++){ //set button array with same color
                for(int j=0;j<N;j++){
                    Color prevColor = buttonGrid[i][j].getBackground();
                    buttonGrid[i][j] = new JButton(String.valueOf(new DecimalFormat("##0.00").format(prob_matrix[i][j])));
                    buttonGrid[i][j].addActionListener(buttonListener);
                    buttonGrid[i][j].setOpaque(true);
                    buttonGrid[i][j].setBackground(prevColor);
                    if(prevColor == Color.cyan && prob_matrix[i][j]==0) buttonGrid[i][j].setBackground(new Color(220,250,250));
                }
            }
            buttonGrid[sensed_y][sensed_y].setOpaque(true);
            if(color==0) buttonGrid[sensed_x][sensed_y].setBackground(Color.RED);
            else if(color==1) buttonGrid[sensed_x][sensed_y].setBackground(Color.ORANGE);
            else if(color==2) buttonGrid[sensed_x][sensed_y].setBackground(new Color(0,250,0));

        }
        else{
            Main.attemptCount++;
            frame.setTitle("Ghost Hunter   Attempt: "+Main.attemptCount);
            if(catchAction()){ //hit
                for(int i=0;i<M;i++){ //set button array with same color
                    for(int j=0;j<N;j++){
                        buttonGrid[i][j] = new JButton(String.valueOf(new DecimalFormat("##0.00").format(prob_matrix[i][j])));
                        buttonGrid[i][j].setBackground(Color.YELLOW);
                    }
                }
                buttonGrid[sensed_x][sensed_y].setText("Hit!");
                buttonGrid[sensed_x][sensed_y].setBackground(new Color(200,0,200));
                JOptionPane.showMessageDialog(null, "You have successfully caught the ghost after "+Main.attemptCount+" attempts!");
                Main.attemptCount = 0;
            }
            else{ //miss
                for(int i=0;i<M;i++){ //set button array with same color
                    for(int j=0;j<N;j++){

                        buttonGrid[i][j] = new JButton(String.valueOf(new DecimalFormat("##0.00").format(prob_matrix[i][j])));
                        buttonGrid[i][j].addActionListener(buttonListener);
                        buttonGrid[i][j].setOpaque(true);
                        if(prob_matrix[i][j]==0) buttonGrid[i][j].setBackground(new Color(220,250,250));
                        else buttonGrid[i][j].setBackground(Color.cyan);
                    }
                }
                buttonGrid[sensed_x][sensed_y].setText("Missed!");
                buttonGrid[sensed_x][sensed_y].setBackground(Color.WHITE);
            }
        }


        for(int i=0;i<M;i++){ //set button array with changed color
            for(int j=0;j<N;j++){
                buttonPanel2.add(buttonGrid[i][j]);
            }
        }

        System.out.println("Ghost position: "+ghost_x+" "+ghost_y);
        printProbMatrix();
        containerPanel.add(buttonPanel2);
        containerPanel.revalidate();
        containerPanel.repaint();

        frame.getContentPane().add(containerPanel);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.invalidate();
        frame.validate();
        frame.repaint();
        frame.pack();
        frame.setVisible(true);
    }
    public void setGridProb( JFrame frame,JPanel containerPanel, JPanel buttonPanel, ActionListener listener){
        JButton[][] buttonGrid = new JButton[M][N];

        ActionListener buttonListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                JButton selectedBtn = (JButton) evt.getSource();
                for (int row = 0; row < buttonGrid.length; row++) {
                    for (int col = 0; col < buttonGrid[row].length; col++) {
                        if (buttonGrid[row][col] == selectedBtn) {
                            sensed_x = row;
                            sensed_y = col;
                            System.out.printf("Selected row and column: %d %d%n", row, col);
                        }
                    }
                }//end of for loop
                change(buttonGrid,frame,containerPanel,buttonPanel);
            }
        };
        for(int i=0;i<M;i++){ //set button array with same color
            for(int j=0;j<N;j++){

                buttonGrid[i][j] = new JButton(String.valueOf(new DecimalFormat("##0.00").format(prob_matrix[i][j])));
                buttonGrid[i][j].addActionListener(buttonListener);
                if(prob_matrix[i][j]==0) buttonGrid[i][j].setBackground(new Color(220,250,250));
                else buttonGrid[i][j].setBackground(Color.cyan);
                buttonPanel.add(buttonGrid[i][j]);
            }
        }
    }

    public void setM(int m) {
        M = m;
    }

    public void setN(int n) {
        N = n;
    }

    public void setProb_matrix(double[][] prob_matrix) {
        this.prob_matrix = prob_matrix;
    }

    public void setDis_matrix(int[][] dis_matrix) {
        this.dis_matrix = dis_matrix;
    }

    public int getM() {
        return M;
    }

    public int getN() {
        return N;
    }

    public double[][] getProb_matrix() {
        return prob_matrix;
    }

    public int[][] getDis_matrix() {
        return dis_matrix;
    }

    public int getGhost_x() {
        return ghost_x;
    }

    public int getGhost_y() {
        return ghost_y;
    }
}
