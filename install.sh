#install prodigal gene prediction software
tar -xzvf thirdPartyTools/prodigal.v*.tar.gz ; cd prodigal.v*/ ; cd prodigal.v* ; make ; mv prodigal ../bin/ ; cd .. ; rm -rf prodigal.v*
#install muscle multiple sequence aligner
tar -xzvf thirdPartyTools/muscle3.8.31_src.tar.gz ; cd muscle*/src ; make ; cp muscle ../../bin ; cd ../.. ; rm -rf muscle* 
