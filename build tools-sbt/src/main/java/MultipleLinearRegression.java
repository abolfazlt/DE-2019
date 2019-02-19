import Jama.Matrix;
import Jama.QRDecomposition;

public class MultipleLinearRegression {
	private final Matrix COEFFICIENTS; // regression coefficients

	public MultipleLinearRegression(double[][] x, double[] y) {
		if (x.length != y.length)
			throw new RuntimeException("dimensions don't agree");
		Matrix X = new Matrix(x);
		Matrix Y = new Matrix(y, y.length);
		QRDecomposition qr = new QRDecomposition(X);
		COEFFICIENTS = qr.solve(Y);
	}

	public double beta(int j) {
		return COEFFICIENTS.get(j, 0);
	}

	public double predict(double[] vector) {
		double predict = 0.0;
		for (int i = 0; i < vector.length; i++) {
			predict += beta(i) * vector[i];
		}
		return predict;
	}

	public static void main(String[] args) {
		double[][] x = { { 1, 10, 20 }, { 1, 20, 40 }, { 1, 40, 15 },
				{ 1, 80, 100 }, { 1, 160, 23 }, { 1, 200, 18 } };
		double[] y = { 243, 483, 508, 1503, 1764, 2129 };
		MultipleLinearRegression regression = new MultipleLinearRegression(x, y);
		System.out.println(regression.predict(x[0]));

	}
}




