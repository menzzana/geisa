package se.kirc.math.analysis;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class LogitRealFunction implements UnivariateRealFunction {
	/**
	 * f(z) = e^x / (e^x + 1)
	 */
	public double value(double x) throws FunctionEvaluationException {
		return Math.exp(x) / (Math.exp(x) + 1);
	}
}
