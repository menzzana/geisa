package se.kirc.geisa;

/**
 * The Calculated data class in the application (driver).
 * 
 * Upgrade to Geisa
 * @author Henric Zazzi <henric@zazzi.se>
 * 
 */
public class PermutationData {
	double original_value;
	boolean empty;
	int n_perm,n;
	
	public PermutationData() {
		n_perm=n=0;
		n=0;
		original_value=0;
		empty=true;
	}
	
	public void clear() {
		n_perm=n=0;
		n=0;
		original_value=0;
		empty=true;
	}
	
	public void setValue(String s1) {
		if (s1==null || s1.length()==0)
			return;
		original_value=Double.parseDouble(s1);
		empty=false;
	}
	
	public void setDefaultConvergenceThreshold(double threshold) {
		original_value=threshold;
		empty=false;
	}
	
	public void setPositivePermutation(String resultCell,boolean stableAdditive,boolean stableMultiplicative,ResultColumn column,boolean noCalcNegativeAPP) {
		n++;
		if (resultCell==null || resultCell.length()==0)
			return;
		if (!stableAdditive && isAdditiveResult(column))
			return;
		if (!stableMultiplicative && isMultiplicativeResult(column))
			return;
		if (column.name().equals("APP") && noCalcNegativeAPP)
			return;
		if (column.name().startsWith("OR"))
			if (Math.abs(1-Double.parseDouble(resultCell))>=Math.abs(1-original_value))
				n_perm++;			
		if (column.name().equals("APP") || column.name().equals("MULT"))
			if (Double.parseDouble(resultCell)<=original_value)
				n_perm++;
		if (column.name().startsWith("STABLELR") && 
				resultCell.equals(Messages.getString("status.iteration.converging.yes")))
			n_perm++;
	}
	
	String PermutationPValue() {
		if (empty)
			return "NA";
		return Double.toString(n==0?0:(double)n_perm/(double)n);
	}
	
	boolean isAdditiveResult(ResultColumn column) {
		return column.ordinal()<ResultColumn.STABLELRA.ordinal();
	}
	
	boolean isMultiplicativeResult(ResultColumn column) {
		return column.ordinal()<ResultColumn.STABLELRM.ordinal() &&
				column.ordinal()>ResultColumn.STABLELRA.ordinal();
	}
	
}
