WHAT IS GEISA?
--------------

This is the file README for the GEISA distribution, version 0.1.3.

GEISA is an update of JEIRA.
JEIRA is a Java implementation of GEIRA, a gene-environment interaction 
analysis tool written for R. It adds new capabilities, e.g. permutations
which was not present in the original implementation.


USAGE
-----

usage: geisa [OPTIONS]
 -a,--batchsize <count>             Specifies the number of calculations
                                    to perform to in each submission task.
                                    Default: 200
 -an, --appnegative                 Set this flag if negative APP values should
                                    be included in total permutation
                                    calculations. Default: no
 -b,--bfile <basename>              Specifies the base name of the binary
                                    input files (i.e. the name of the
                                    files without their file extensions:
                                    .bed, .bim, .fam).
 -c,--cutoff <n>                    Specifies the minimum number of
                                    individuals in a group. The
                                    individuals are divided into groups
                                    (case/controls with and without the
                                    environmental factor). If any of these
                                    groups have a count below this value,
                                    no analysis will be performed on that
                                    marker. Default: 5
 -d,--model <type>                  The model type to use (i.e. "dom" for
                                    dominant-dominant or rec for
                                    "recessive".
 -h,--help                          Displays this help text.
 -i,--ifile <file>                  Specifies the input interaction
                                    variable file. Default: null
 -l,--limitfile <file>              specifies a file containing
                                    significance limits for APp and MULT
                                    permutation calculations.
 -lri,--iteration <iteration>       Sets the max number of iteration to
                                    perform when computing logistic
                                    regression (Default: 500)
 -lrt,--threshold <threshold>       Sets the min stable threshold when
                                    computing logistic regression
                                    (Default: 10E-3)
 -m,--markerfile <file>             Specifies a file containing
                                    interaction markers targeted for
                                    analysis.
 -o,--output <path>                 Specifies the directory where the
                                    output files will be stored. Default:
                                    None (Creates a result directory
                                    automatically)
 -p,--permutations <count>          Specifies the number of case/control
                                    permutations to perform. Default: 0
 -po,--permutationoutput <R=raw permutation output, T=total permutation output
                                    Sets if permutation rawdata should be
                                    printed to various files (Default: No)
                                    With Total permutation output, the first
                                    row shows the original data, whereas
                                    the other rows are the permutated data
 -q,--queuesize <count>             Specifies the maximum amount of tasks
                                    to keep in the internal task queue.
                                    Default: available cores * 20
 -s,--seed <value>                  Specifies the seed used by the PRNG.
                                    Default: current system time
                                    milliseconds.
 -t,--datastoretype <type>          Specifies what data store type to use.
                                    Memory (m) or File (f). Default: m
 -w,--workers <count>               Worker thread count. Default: number
                                    of available processors/cores.      

RECODE
------

If the presence of the risk allele is determined to be protective, a recode
is performed in such a way that what's considered a risk factor is reassessed. 
The recode is denoted in the output column "recode" with the following 
possible values:

   0 - No recode is performed.
   1 - The risk allele is considered to have a protective effect, and the risk 
       factor will be thought of as the absence of the risk allele. E.g. if 
       alleles A and T are present and A was initially considered the risk 
       allele, the absence of A will now be denoted the risk factor.
   2 - The interaction variable is inverted.
   3 - This is a combination of recode 1 and 2.

The column denoting the risk allele in the output will remain the same even 
after recoding. So in the case of a recode 1 or 3, the risk allele is in fact
considered protective.

PACKAGE
-------

This package contains 3 folders.

dist      Binary files distribution
samples   A colleaction of sample input files
src       All the source files for Geisa

UNIQUE FILE FORMAT
------------------

Beside the standard binary input files, Geisa also contains other files
to interact with the data.

INTERACTION VARIABLE FILE

The interaction variable files contains individual IDs, environment variable and
covariates.
The first line of the file should depict the specific column data, and all
columns should be separated by TAB.
Individuals columns should be name INDID.
Environment variable should be name ENV
All other columns will be treated as covariate columns

Example.

INDID ENV COV1  TEST  HELLO
04D01801	0  1 0 1
 
First column is Individual ID, and 2nd is Environment.
COV1, TEST and HELLO are all covariates.

LIMIT FILE

The limit file contain only 2 columns.
The first line of the file should depict the specific column data, and all
columns should be separated by TAB.
The cutoff column for AP_pvalue should be named CUTOFF_APP
whereas the Multiplicative_interaction_term_pvalue cutoff column should
be name CUTOFF_MULT.
As many cutoff values as wanted can be added.

INTERACTION MARKER FILE

Should only containt one column with marker names.


COPYRIGHT
---------

JEIRA was written by Daniel Uvehag, and is available under the GNU General 
Public License.
GEISA is an update of JEIRA written by Henric Zazzi.

daniel.uvehag@ki.se
henric@zazzi.se


AVAILABILITY
------------

The main web site for GEISA is https://github.com/menzzana/geisa
