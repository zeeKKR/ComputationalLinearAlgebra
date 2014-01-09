import java.util.*;
import java.io.*;
/**
 * Created with IntelliJ IDEA.
 * User: zKK
 * Date: 10/29/13
 * Time: 8:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompleteSolution {
    private final String inputFile;
    private int m;
    private int n;
    private double[][] matrix;

    public static void main(String[] args) throws IOException{
       new CompleteSolution(args[0]).solve();
    }

    public CompleteSolution(String arg0) throws IOException{
        inputFile = arg0;
    }

    private void solve() throws IOException{
        retrieveValues();
        System.out.println("The input matrix is: ");
        printMatrix(matrix);
        System.out.println();
        elimination(matrix);
        System.out.println("The matrix after elimination is: ");
        printMatrix(matrix);
        findNumberOfSols(matrix);
    }

    private void retrieveValues() throws FileNotFoundException{
        Scanner s = new Scanner(new BufferedReader(new FileReader(inputFile)));
        m = s.nextInt();
        n = s.nextInt();
        matrix = new double[m][n+1];
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                matrix[i][j] = s.nextDouble();
            }
        }
        for(int i = 0; i < m; i++){
            matrix[i][n] = s.nextDouble(); //n is 3 which is last element of length 4 array
        }
        s.close();
    }



    private void printMatrix(double[][] inputMatrix){
        for(int i = 0; i < m; i++){
            for(int j = 0; j < (n + 1); j++){
                System.out.printf("%6.2f ", inputMatrix[i][j]);
            }
            System.out.println();
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

    public  void elimination(double[][] inputMatrix){
        int maxPivot = Math.min(m, n);
        double pivot = 0;
        int z = 0;

        for (int i = 0; i < maxPivot && z < n; i++){ //last row gets updated with n-1th row
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
                    while(inputMatrix[i][z] == 0 && z < (n-1)){
                        z++;
                    }
                    if(z < n){
                        pivot = inputMatrix[i][z];
                    }
                }
                else{
                    pivot = inputMatrix[i][z];
                }
            }
            //At this point we've found the pivot
            //Elimination
            if(z < n && pivot != 0){
                for(int j = (i + 1); j < m; j++){
                    double multiplier = -1 * inputMatrix[j][z] / pivot;
                    for(int k = z; k <= n; k++){
                        inputMatrix[j][k] += multiplier * inputMatrix[i][k];
                    }
                }
            }
        }
    }

    public void findNumberOfSols(double[][] inputMatrix){
        boolean noSol = false;
        double[] b = new double[m];
        int numOfPivots = 0;
        //int maxPivots = Math.min(m, n);

        for(int i = 0; i < m; i++){
            b[i] = inputMatrix[i][n];
        }

        for(int i = 0; i < m; i++){
            for(int j = i; j < n; j++){
                if(inputMatrix[i][j] != 0){
                    numOfPivots++;
                    break;
                }
                else{
                    while(inputMatrix[i][j] == 0 && j < (n - 1)){
                        j++;
                    }
                    if(inputMatrix[i][j] != 0){
                        numOfPivots++;
                        break;
                    }
                }
            }
        }

        if(inputMatrix.length == inputMatrix[0].length - 1 && numOfPivots == n){ //
            System.out.println("There is one solution: ");
            double[] solVector = new double[m];
            for(int i = (m - 1); i >= 0; i--){
                double sum = 0;
                for(int j = i + 1; j < m; j++){
                    sum += inputMatrix[i][j] * solVector[j];
                }
                solVector[i] = (b[i] - sum) / inputMatrix[i][i];
            }

            for(int i = 0; i < solVector.length; i++){
                System.out.println(solVector[i]);
            }
        }
        else if(numOfPivots == n && numOfPivots < m){
            int potentialZeroRows= m - numOfPivots;
            for(int i = b.length - 1; i > ((b.length - 1) - potentialZeroRows); i--){
                if(b[i] != 0){
                    System.out.println("There is no solution");
                    noSol = true;
                    break;
                }
            }
            if(!noSol){
                System.out.println("There is one solution: ");
                double[] solVector = new double[m - potentialZeroRows];
                for(int i = (m - 1 - potentialZeroRows); i >= 0; i--){
                    double sum = 0;
                    for(int j = i + 1; j < m - potentialZeroRows; j++){
                        sum += inputMatrix[i][j] * solVector[j];
                    }
                    solVector[i] = (b[i] - sum) / inputMatrix[i][i];
                }

                for(int i = 0; i < solVector.length; i++){
                    System.out.println(solVector[i]);
                }
            }
        }

        else if(numOfPivots == m && numOfPivots < n){
            double divideBy = 0;
            System.out.println("There are infinite solutions");
            System.out.println("The RREF is: ");
            boolean contFlag = false;
            for(int i = 0; i < m; i++){
                contFlag = false;
                for(int j = i; j < n; j++){
                    if(inputMatrix[i][j] != 0){
                        divideBy = inputMatrix[i][j];
                        contFlag = true;
                    }
                    if(contFlag){
                        for(int k = i; k <(n + 1); k++){
                            if(divideBy != 0){
                                inputMatrix[i][k] = (inputMatrix[i][k] / divideBy);
                            }
                        }
                        break;
                    }
                }
            }

            boolean firstTime = true;
            for(int i = m - 1; i >= 0; i--){
                firstTime = true;
                for(int j = 0; j < n; j++){
                    if(inputMatrix[i][j] != 0 && firstTime){
                        for(int k = (i - 1); k >= 0; k--){
                            double pivot = -inputMatrix[k][j];
                            firstTime = false;
                            for(int l = j; l < (n + 1); l++){
                                if(pivot != 0){
                                    inputMatrix[k][l] += pivot * inputMatrix[i][l];
                                }
                            }
                        }
                    }
                }
            }

            printMatrix(inputMatrix);

            System.out.println("A particular solution is: ");
            int i = 0;
            int j = 0;
            boolean[] fpArray = new boolean[n];
            while(j < n && i < m){
                if(inputMatrix[i][j] != 0){
                    fpArray[j] = true; //true means pivot
                    i++;
                    j++;
                }
                else{
                    while(inputMatrix[i][j] == 0 && j < n){
                        fpArray[j] = false; //false means free
                        j++;
                    }
                }
            }
            int counter = 0;

            for(int r = 0; r < fpArray.length; r++){
                if(fpArray[r]){
                    System.out.printf("%.2f\n", inputMatrix[counter][n]);
                    counter++;
                }
                else{
                    System.out.println("0");
                }
            }
            //STILL TO IMPLEMENT
            System.out.println("The special solution(s) is/are: ");
            for(int t = 0; t< fpArray.length; t ++){
                int counterNullspace = 0;
                if(fpArray[t] == false){
                    System.out.println();
                    for(int y = 0; y < n; y++){
                        if(fpArray[y] == false && y == t){
                            System.out.println(1);
                        }
                        else if(fpArray[y] == true){
                            System.out.printf("%.2f\n", (-1 * inputMatrix[counterNullspace][t]));
                            counterNullspace++;
                        }
                        else{
                            System.out.println(0);
                        }
                    }
                }
            }

        }
        else{
            int potentialZeroRows= m - numOfPivots;
            for(int i = b.length - 1; i > ((b.length - 1) - potentialZeroRows); i--){
                if(b[i] != 0){
                    System.out.println("There is no solution");
                    noSol = true;
                    break;
                }
            }
            if(!noSol){
                double divideBy = 0;
                System.out.println("There are infinite solutions");
                System.out.println("The RREF is: ");
                boolean contFlag = false;
                for(int i = 0; i < m; i++){
                    contFlag = false;
                    for(int j = i; j < n; j++){
                        if(inputMatrix[i][j] != 0){
                            divideBy = inputMatrix[i][j];
                            contFlag = true;
                        }
                        if(contFlag){
                            for(int k = i; k <(n + 1); k++){
                                if(divideBy != 0){
                                    inputMatrix[i][k] = (inputMatrix[i][k] / divideBy);
                                }
                            }
                            break;
                        }
                    }
                }
                boolean firstTime = true;
                for(int i = m - 1; i >= 0; i--){
                    firstTime = true;
                    for(int j = 0; j < n; j++){
                        if(inputMatrix[i][j] != 0 && firstTime){
                            for(int k = (i - 1); k >= 0; k--){
                                double pivot = -inputMatrix[k][j];
                                firstTime = false;
                                for(int l = j; l < (n + 1); l++){
                                    if(pivot != 0){
                                        inputMatrix[k][l] += pivot * inputMatrix[i][l];
                                    }
                                }
                            }
                        }
                    }
                }

                printMatrix(inputMatrix);

                //STILL TO IMPLEMENT
                System.out.println("A particular solution is: ");
                int i = 0;
                int j = 0;
                boolean[] fpArray = new boolean[n];
                while(j < n && i < m){
                    if(inputMatrix[i][j] != 0){
                        fpArray[j] = true; //true means pivot
                        i++;
                        j++;
                    }
                    else{
                        while(inputMatrix[i][j] == 0 && j < n){
                            fpArray[j] = false; //false means free
                            j++;
                        }
                    }
                }
                int counter = 0;

                for(int r = 0; r < fpArray.length; r++){
                    if(fpArray[r]){
                        System.out.printf("%.2f\n",inputMatrix[counter][n]);
                        counter++;
                    }
                    else{
                        System.out.println("0");
                    }
                }
                //STILL TO IMPLEMENT
                System.out.println("The special solutions are: ");
                for(int t = 0; t< fpArray.length; t ++){
                    int counterNullspace = 0;
                    if(fpArray[t] == false){
                        System.out.println();
                        for(int y = 0; y < n; y++){
                            if(fpArray[y] == false && y == t){
                                System.out.println(1);
                            }
                            else if(fpArray[y] == true){
                                System.out.printf("%.2f\n", -1 * inputMatrix[counterNullspace][t]);
                                counterNullspace++;
                            }
                            else{
                                System.out.println(0);
                            }
                        }
                    }
                }
            }
        }


    }
}
