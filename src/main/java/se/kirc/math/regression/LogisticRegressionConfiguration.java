package se.kirc.math.regression;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;

public class LogisticRegressionConfiguration {
	public final static int DEFAULT_MAX_ITERATIONS = 500;
	public final static double DEFAULT_CONVERGENCE_THRESHOLD = 1e-3;

	private RealMatrix x;
	private RealVector y;
	private RealVector beta;
	private int iterations;
	private double convergenceThreshold;

	/**
	 * 
	 */
	public LogisticRegressionConfiguration() {
	}
	
	/**
	 * @return the x
	 */
	public RealMatrix getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(RealMatrix x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public RealVector getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(RealVector y) {
		this.y = y;
	}

	/**
	 * @return the beta
	 */
	public RealVector getBeta() {
		return beta;
	}

	/**
	 * @param beta
	 *            the beta to set
	 */
	public void setBeta(RealVector beta) {
		this.beta = beta;
	}

	/**
	 * @return the iterations
	 */
	public int getIterations() {
		return iterations;
	}

	/**
	 * @param iterations
	 *            the iterations to set
	 */
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	/**
	 * @return the convergenceThreshold
	 */
	public double getConvergenceThreshold() {
		return convergenceThreshold;
	}

	/**
	 * @param convergenceThreshold
	 *            the convergenceThreshold to set
	 */
	public void setConvergenceThreshold(double convergenceThreshold) {
		this.convergenceThreshold = convergenceThreshold;
	}
}
