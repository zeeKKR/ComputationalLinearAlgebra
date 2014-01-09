import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zKK
 * Date: 12/2/13
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class InverseIteration {
    public static void main(String[] args){
        double[][] matrix = new double[10][10];
        double[][] cholReg = new double[10][10];
        double[][] cholTranspose = new double[10][10];
        double[] unitVector = new double[10];
        //first row
        matrix[0][0] = 2;
        matrix[0][1] = -1;
        for(int i = 1; i < 9; i++){
            int j = i - 1;
            matrix[i][j++] = -1;
            matrix[i][j++] = 2;
            matrix[i][j] = -1;
        }
        //last row
        matrix[9][8] = -1;
        matrix[9][9] = 2;

        unitVector = randomizeVec(10);

        //printMatrix(matrix);
        //System.out.println();
        cholReg = choleskyDecomp(matrix);
        //printMatrix(cholReg);
        //System.out.println();
        cholTranspose = transpose(cholReg);
        //printMatrix(cholTranspose);

        //changing matrix back to A
        matrix[0][0] = 2;
        matrix[0][1] = -1;
        for(int i = 1; i < 9; i++){
            int j = i - 1;
            matrix[i][j++] = -1;
            matrix[i][j++] = 2;
            matrix[i][j] = -1;
        }
        matrix[9][8] = -1;
        matrix[9][9] = 2;

        double[] temp = new double[10];
        double[] x = new double[10];
        x = unitVector;
        double[] y = new double[10];
        double[] multTemp = new double[10];
        double eigenvalue = 0;
        double prevEigenvalue = 0;
        do{
            prevEigenvalue = eigenvalue;
            temp = forwardSub(cholReg, x);
            y = backSub(cholTranspose, temp);
            x = normalize(y);
            multTemp = vectMult(matrix, x);
            eigenvalue = dotProduct(multTemp, x);
        }
        while(eigenvalue - prevEigenvalue > 0.001);

        System.out.printf("The smallest eigenvalue is approximately: %.3f.", eigenvalue);
    }

    private static void printMatrix(double[][] inputMatrix){
        for(int i = 0; i < inputMatrix.length; i++){
            for(int j = 0; j < inputMatrix[0].length; j++){
                System.out.printf("%5.2f ", inputMatrix[i][j]);
            }
            System.out.println();
        }
    }

    private static double[][] choleskyDecomp(double[][] inputMatrix){
        int size = inputMatrix.length; //square
        double[] temp = new double[size];

        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                double sum = inputMatrix[i][j];
                for (int k = i - 1; k >= 0; k--)
                    sum -= inputMatrix[i][k] * inputMatrix[j][k];
                if (i == j) {
                    temp[i] = Math.sqrt(sum);
                } else
                    inputMatrix[j][i] = sum / temp[i];
            }
        }
        for (int a = 0; a < size; a++) {
            inputMatrix[a][a] = temp[a];
            for (int b = a + 1; b < size; b++)
                inputMatrix[a][b] = 0;
        }
        return inputMatrix;
    }

    private static double[][] transpose(double[][] matrix){
       double[][] transpose = new double[matrix[0].length][matrix.length];

        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                transpose[i][j] = matrix[j][i];
            }
        }
        return transpose;
    }

    public static double[] randomizeVec(int length){
        double[] unitVector = new double[length];
        Random generator = new Random();
        for(int i  = 0; i < length; i++){
            unitVector[i] = generator.nextInt(11); //generates 1-10
        }
        double sum = 0;

        for(int i = 0; i < length; i++){
            sum += unitVector[i] * unitVector[i];
        }

        for(int i = 0; i < length; i++){
            unitVector[i] = unitVector[i] / Math.sqrt(sum);
        }
        return unitVector;
    }

    private static double[] vectMult(double[][] matrix, double[] vector){
        double[] result = new double[vector.length];
        double sum = 0;
        for(int i = 0; i < vector.length; i++){
            for(int j = 0; j < matrix.length; j++){
                sum += matrix[i][j] * vector[j];
            }
            result[i] = sum;
            sum = 0;
        }

        return result;
    }

    public static double[] normalize (double[] vector){
        double sum = 0;
        for(int i = 0; i < vector.length; i++){
            sum += vector[i] * vector[i];
        }

        for(int i  = 0; i < vector.length; i++){
            vector[i] = vector[i] / Math.sqrt(sum);
        }

        return vector;
    }

    public static double dotProduct(double[] vector1, double[] vector2){
        double sum = 0;
        for(int i = 0; i < vector1.length; i++){
            sum += vector1[i] * vector2[i];
        }

        return sum;
    }

    public static double[] forwardSub(double[][] matrix, double[] sol){
        double[] result = new double[sol.length];
        double sum = 0;

        for(int i = 0; i < sol.length; i++){
            sum = 0;
            for(int j = 0; j < i; j++){
                sum += matrix[i][j] * result[j];
            }
            result[i] = (sol[i] - sum) / matrix[i][i];
        }
        return result;
    }

    public static double[] backSub(double[][] matrix, double[] sol){
        double[] result = new double[sol.length];
        double sum = 0;

        for(int i = sol.length - 1; i >= 0; i--){
            sum = 0;
            for(int j = sol.length - 1; j > i; j--){
                sum += matrix[i][j] * result[j];
            }
            result[i] = (sol[i] - sum) / matrix[i][i];
        }

        return result;
    }
}
