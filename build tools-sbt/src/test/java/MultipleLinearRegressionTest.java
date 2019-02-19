import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MultipleLinearRegressionTest {

	@Test
	public void simpleCheck() {
		double[][] x = { { 2 }, { 3 } };
		double[] y = { 4, 6 };
		MultipleLinearRegression regression = new MultipleLinearRegression(x, y);
		double[] test = { 4 };
		assertEquals(8, regression.predict(test), 0.0);
	}

}
