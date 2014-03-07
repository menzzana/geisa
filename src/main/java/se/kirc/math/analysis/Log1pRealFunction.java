package se.kirc.math.analysis;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class Log1pRealFunction implements UnivariateRealFunction {
	public double value(double x) throws FunctionEvaluationException {
		return Math.log1p(x);
	}

}
