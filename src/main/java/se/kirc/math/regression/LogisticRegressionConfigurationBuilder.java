package se.kirc.math.regression;

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;

public class LogisticRegressionConfigurationBuilder {
	private RealMatrix x;
	private RealVector y;
	private RealVector beta;
	private int iterations;
	private double convergenceThreshold;

	public LogisticRegressionConfigurationBuilder() {
		// Initialize default values.
		convergenceThreshold = LogisticRegressionConfiguration.DEFAULT_CONVERGENCE_THRESHOLD;
		iterations = LogisticRegressionConfiguration.DEFAULT_MAX_ITERATIONS;
	}

	public LogisticRegressionConfigurationBuilder withX(RealMatrix x) {
		this.x = x;

		return this;
	}

	public LogisticRegressionConfigurationBuilder withY(RealVector y) {
		this.y = y;

		return this;
	}

	public LogisticRegressionConfigurationBuilder withBeta(RealVector beta) {
		this.beta = beta;

		return this;
	}

	public LogisticRegressionConfigurationBuilder withConvergenceThreshold(
			double convergenceThreshold) {
		this.convergenceThreshold = convergenceThreshold;

		return this;
	}

	public LogisticRegressionConfigurationBuilder withIterations(
			int iterations) {
		this.iterations = iterations;

		return this;
	}

	public LogisticRegressionConfiguration build() {
		LogisticRegressionConfiguration config = new LogisticRegressionConfiguration();

		// Initialize beta if missing.
		if (beta == null) {
			beta = new ArrayRealVector(x.getRowDimension() + 1);
			beta.set(0);
		}

		config.setBeta(beta);
		config.setConvergenceThreshold(convergenceThreshold);
		config.setIterations(iterations);
		config.setX(x);
		config.setY(y);

		return config;
	}
}
