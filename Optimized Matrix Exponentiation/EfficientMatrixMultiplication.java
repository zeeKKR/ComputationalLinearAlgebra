import java.io.*;
import java.util.*;

public class EfficientMatrixMultiplication{
	private final String inputFile;
	private final String outputFile;
	private int matrixSize;
	private double[][] matrix;
    private double[][] productMatrix;
    private int exponent;

	public static void main(String[] args) throws IOException{
	if(args.length != 2){
		System.out.println("Please only enter the name of the input and output files");
		System.exit(1);
	}

	new EfficientMatrixMultiplication(args[0], args[1]).solve();
	}

	public EfficientMatrixMultiplication(String arg0, String arg1){
	inputFile = arg0;
	outputFile = arg1;
	}
	
	private void solve() throws IOException{
	retrieveValues();
	optimizedMatrixMultiplication(matrix, matrix, 1);
    exportToFile();
	}

	private void retrieveValues() throws FileNotFoundException{
		Scanner s = new Scanner(new BufferedReader(new FileReader(inputFile)));
	matrixSize = s.nextInt();
	exponent = s.nextInt();
	matrix = new double[matrixSize][matrixSize]; 
	while(s.hasNextDouble()){
		for(int i = 0; i < matrixSize; i++){
			for(int j = 0; j < matrixSize; j++){
				matrix[i][j] = s.nextDouble();
			}
		}
	}
        s.close();
	}

	private double[][] matrixMultiplication(double[][] matrixA, double[][] matrixB){
        productMatrix = new double[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; i++){
            for (int j = 0; j < matrixSize; j++){
                for (int k = 0; k < matrixSize ; k++){
                    productMatrix[i][j] = productMatrix[i][j] + matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return productMatrix;
	}

    private double[][] optimizedMatrixMultiplication(double[][] matrixA, double[][] matrixB, int k){
       double[][] tempMatrix = new double[matrixSize][matrixSize];
       tempMatrix = matrixMultiplication(matrixA, matrixB);
       if(k < exponent){
           return optimizedMatrixMultiplication(tempMatrix, tempMatrix, k + 1) ;
       }
        else{
           return new double[0][0];
       }
    }

    private void exportToFile() throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for(int i =0; i < matrixSize; i++){
            if(i > 0){
                writer.write("\r\n");
            }
            for(int j =0; j < matrixSize; j++){
                writer.write(productMatrix[i][j] + " ");
            }
        }
        writer.close();
    }
}