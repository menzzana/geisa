package se.kirc.math.analysis;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class ExpRealFunction implements UnivariateRealFunction {
	
	public double value(double x) throws FunctionEvaluationException {
		return Math.exp(x);
	}

}