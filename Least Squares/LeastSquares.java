import java.util.*;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: zooK
 * Date: 11/16/13
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class LeastSquares {
    private final String inputFile;
    private int m;
    private double c;
    private double d;
    private double e;
    private double[] y_i;
    private double[] b_i;
    private double[] t_i;
    private double[][] aMatrix;
    private double[][] aTranspose;
    private double[] aTransposeB;
    private double[][] aTransposeA;
    private double[][] augmentedMatrix;

    public static void main(String[] args) throws IOException{
        new LeastSquares(args[0]).solve();
    }

    public LeastSquares(String arg0) throws IOException{
        inputFile = arg0;
    }

    private void retrieveValues() throws FileNotFoundException{
        Scanner s = new Scanner(new BufferedReader(new FileReader(inputFile)));
        m = s.nextInt();
        c = s.nextDouble();
        d = s.nextDouble();
        e = s.nextDouble();

        s.close();
    }

    private void solve() throws IOException{
        retrieveValues();
        set_y_i_b_i();
        constructA();
        constructATranspose(aMatrix);
        matrixMult(aTranspose, aMatrix);
        vectMult(aTranspose, b_i);
        augment(aTransposeA, aTransposeB);
        elimination(augmentedMatrix);
        printMatrix(augmentedMatrix);
        System.out.println("Calculated coefficients are: ");
        backSubstitution();
        System.out.printf("Original coefficients are: %.2f, %.2f, %.2f", c, d, e);
    }

    private void set_y_i_b_i(){
        y_i = new double[m];
        t_i = new double[m];

        for(int i = m; i > 0; i--){
            y_i[i - 1] = c + d*i + e*i*i;
            t_i[i - 1] = i;
        }

        b_i = new double[m];
        Random generator = new Random();
        for(int i = 0; i < m; i ++){
            if(generator.nextInt(2) == 0){
                b_i[i] = y_i[i] + generator.nextDouble();
            }
            else{
                b_i[i] = y_i[i] - generator.nextDouble();
            }
        }
    }

    private void constructA(){
        aMatrix = new double[m][3];
        for(int i = 0; i < m; i++){
           aMatrix[i][0] = 1;
           aMatrix[i][1] = t_i[i];
           aMatrix[i][2] = t_i[i] * t_i[i];
        }
    }

    private void constructATranspose(double[][] matrix){
        aTranspose = new double[3][m];

        for(int i = 0; i < aTranspose.length; i++){
            for(int j = 0; j < aTranspose[0].length; j++){
                aTranspose[i][j] = matrix[j][i];
            }
        }
    }

    private void matrixMult(double[][] matrix1, double[][] matrix2){
        aTransposeA = new double[3][3];
        for(int i = 0; i < matrix1.length; i++){
            for(int j = 0; j < matrix2[0].length; j++){
                for(int k = 0; k < matrix2.length; k++){
                    aTransposeA[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
    }

    private void vectMult(double[][] matrix, double[] vector){
        aTransposeB = new double[3];
        double result = 0;
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                result += matrix[i][j] * vector[j];
            }
            aTransposeB[i] = result;
            result = 0;
        }
    }

    private void augment(double[][] matrix, double[] vector){
        augmentedMatrix = new double[3][4];
        for(int i = 0; i < matrix.length; i++){
            augmentedMatrix[i][3] = vector[i];
            for(int j = 0; j < matrix[0].length; j++){
                augmentedMatrix[i][j] = aTransposeA[i][j];
            }
        }
    }

    public  void elimination(double[][] inputMatrix){
        int maxPivot = Math.min(inputMatrix.length, inputMatrix[0].length);
        double pivot = 0;
        int z = 0;

        for (int i = 0; i < maxPivot && z < inputMatrix[0].length - 1; i++){ //last row gets updated with n-1th row
            z = i;
            boolean found = false;
            if(inputMatrix[i][z] != 0){
                pivot = inputMatrix[i][z];
            }
            else{
                for(int j = (i+1); j < m; j++){
                    if(inputMatrix[j][i] != 0){
                        swapRows(inputMatrix, i, j);
                        found = true;
                        break;
                    }
                }
                if(!found){
                    while(inputMatrix[i][z] == 0 && z < (inputMatrix[0].length-1)){
                        z++;
                    }
                    if(z < inputMatrix[0].length){
                        pivot = inputMatrix[i][z];
                    }
                }
                else{
                    pivot = inputMatrix[i][z];
                }
            }
            //At this point we've found the pivot
            //Elimination
            if(z < inputMatrix[0].length && pivot != 0){
                for(int j = (i + 1); j < inputMatrix.length; j++){
                    double multiplier = -1 * inputMatrix[j][z] / pivot;
                    for(int k = z; k < inputMatrix[0].length; k++){
                        inputMatrix[j][k] += multiplier * inputMatrix[i][k];
                    }
                }
            }
        }
    }

    private void swapRows(double[][] inputMatrix, int r1, int r2){//THE FIRST ROW IS ROW ZERO. ZERO INDEXING
        double temp;
        for(int i = 0; i < inputMatrix[0].length; i++){
            temp = inputMatrix[r1][i];
            inputMatrix[r1][i] = inputMatrix[r2][i];
            inputMatrix[r2][i] = temp;
        }
    }

    private void printMatrix(double[][] inputMatrix){
        for(int i = 0; i < inputMatrix.length; i++){
            for(int j = 0; j < inputMatrix[0].length; j++){
                System.out.printf("%.2f ", inputMatrix[i][j]);
            }
            System.out.println();
        }
    }

    private void backSubstitution(){
        double[] b = new double[3];
        for(int i = 0; i < 3; i++){
            b[i] = augmentedMatrix[i][3];
        }

        double[] solVector = new double[augmentedMatrix.length];
        for(int i = (augmentedMatrix.length - 1); i >= 0; i--){
            double sum = 0;
            for(int j = i + 1; j < augmentedMatrix.length; j++){
                sum += augmentedMatrix[i][j] * solVector[j];
            }
            solVector[i] = (b[i] - sum) / augmentedMatrix[i][i];
        }

        for(int i = 0; i < solVector.length; i++){
            System.out.println(solVector[i]);
        }
    }

}
