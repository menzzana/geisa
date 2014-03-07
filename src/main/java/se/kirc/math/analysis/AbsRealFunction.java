package se.kirc.math.analysis;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class AbsRealFunction implements UnivariateRealFunction {

	public double value(double x) throws FunctionEvaluationException {
		return Math.abs(x);
	}

}
