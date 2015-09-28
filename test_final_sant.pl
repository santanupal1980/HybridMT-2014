#!/usr/bin/perl
open(INP1, "<:utf8", $ARGV[0]) || die "cant open";
@src = <INP1>;
close INP1;
open(INP2, "<:utf8", $ARGV[1]) || die "cant open";
@tgt = <INP2>;
close INP2;
binmode(STDOUT, "utf8");
my(%atomic_TT,%check_TT,%TT);

$itr_step = 1;
while(1)
{

	$LTT_count = 0;
	$DTT_count = 0;
#	print $DTT_count,"\n";
for($loop1=0; $loop1<=$#src; $loop1++) {
	for($loop2=$loop1+1; $loop2<=$#tgt; $loop2++) {

		$src1 = $src[$loop1];	
		$src2 = $src[$loop2];
		@tok1 =  split(/\s+/,$src1);
		@tok2 =  split(/\s+/,$src2);
		my $src_ratio = ($#tok1+1)/($#tok2+1);
		if($src_ratio >=2 || $src_ratio <=0.5){ last;}	
		$seq_a = MS_STTL(\@tok1,\@tok2); # call subroutine for similar matching sentence
		#print $seq_a,"\n";
		$tgt1 = $tgt[$loop1];	
		$tgt2 = $tgt[$loop2];	
		@tok1 =  split(/\s+/,$tgt1);
		@tok2 =  split(/\s+/,$tgt2);
		my $tgt_ratio = ($#tok1+1)/($#tok2+1);
		if($tgt_ratio >=2 || $tgt_ratio <=0.5){ last;}	
		$seq_b = MS_STTL(\@tok1,\@tok2);
		#print $seq_b,"\n";

		#print $src1,"\t",$tgt1,"\n";
		#print $src2,"\t",$tgt2,"\n";
		#print $seq_a,"\t",$seq_b,"\n";
		if( $seq_a !~ /null/ && $seq_b !~ /null/)
		{
			learn_STTL($seq_a,$seq_b);
			learn_DTTL($seq_a,$seq_b);
		}
		#print $loop1," ",$loop2,"\n";
	}
}

#print $DTT_count,"\n";
if(($DTT_count == 0 && $LTT_count == 0 ) || $itr_step==$ARGV[2]) {last;}
$itr_step++;
}

#print "---------Translation templates--------\n";

while(my($key,$val) = each(%TT))
{
    print $key,"\t",$val,"\n";
    #if($key!~/X2/ && $val !~ /X2/)
    #{
    #	$key =~ s/X1//g;
    #	$val =~ s/X1//g;
    #	$key =~ s/\s+/ /g;
    #	$val =~ s/\s+/ /g;
    #	$key =~ s/^\s/ /g;
    #	$val =~ s/^\s/ /g;
    #	$key =~ s/\s$/ /g;
    #	$val =~ s/\s$/ /g;
    #	print $key,"\t",$val,"\n";
    #}
}

#print "---------automic templates--------\n";
while(my($key,$val) = each(%atomic_TT))
{
	print $key,"\t",$val,"\n";
}

sub learn_STTL
{
	my($ms_1, $ms_2) = @_;

	my($prev_flag1,$prev_flag2);
	$h1 = $ms_1; $h2=$ms_2;
	#print $ms_1,"\t",$ms_2,"\n";
	$ms_1 =~ s/^\s+//g;
	$ms_2 =~ s/^\s+//g;
	$ms_1 =~ s/\s+$//g;
	$ms_2 =~ s/\s+$//g;
	my(@diff1,@diff2);
	my(@sim1,@sim2);	
	my @token_1 = split(/\)/,$ms_1);
	my @token_2 = split(/\)/,$ms_2);
	for($i=0; $i<=$#token_1; $i++)
	{
		$prev_flag1=0;
		if( $token_1[$i] =~ /\(/)
		{
			my($sim,$diff) = split(/\(/,$token_1[$i]);
			push(@diff1,$diff);
			#print ">>>$sim<<<\n";
			if($sim ne '') {
				push(@sim1,$sim);
				#this block checks if sim matches with pre diff
				my @tmp_sim = split(/\s+/,$sim);
				my $tmp_diff = join("",@diff1);
				$tmp_diff =~ s/\|/ /g;
				my @tmp_diff = split(/\s+/,$tmp_diff);
				my %in_array2 = map { $_ => 1 } @tmp_sim;
				my @array3 = grep { $in_array2{$_} } @tmp_diff;
				if($#array3 >=0) {$prev_flag1=1; last;}
				#upto here
			}
		}
		else
		{
			push(@sim1,$token_1[$i]);
				#this block checks if sim matches with pre diff
				my @tmp_sim = split(/\s+/,$token_1[$i]);
				my $tmp_diff = join("",@diff1);
				$tmp_diff =~ s/\|/ /g;
				my @tmp_diff = split(/\s+/,$tmp_diff);
				my %in_array2 = map { $_ => 1 } @tmp_sim;
				my @array3 = grep { $in_array2{$_} } @tmp_diff;
				if($#array3 >=0) {$prev_flag1=1; last;}
				#upto here
		}	
	}
	for($i=0; $i<=$#token_2; $i++)
	{
		my $prev_flag=0;
		if( $token_2[$i] =~ /\(/)
		{
			my($sim,$diff) = split(/\(/,$token_2[$i]);
			push(@diff2,$diff);
			if($sim ne '') {
				push(@sim2,$sim);
				#this block checks if sim matches with pre diff
				my @tmp_sim = split(/\s+/,$sim);
				my $tmp_diff = join("",@diff2);
				$tmp_diff =~ s/\|/ /g;
				my @tmp_diff = split(/\s+/,$tmp_diff);
				my %in_array2 = map { $_ => 1 } @tmp_sim;
				my @array3 = grep { $in_array2{$_} } @tmp_diff;
				if($#array3 >=0) {$prev_flag2=1; last;}
				#upto here
			}
		}
		else
		{
			push(@sim2,$token_2[$i]);
				#this block checks if sim matches with pre diff
				my @tmp_sim = split(/\s+/,$token_2[$i]);
				my $tmp_diff = join("",@diff2);
				$tmp_diff =~ s/\|/ /g;
				my @tmp_diff = split(/\s+/,$tmp_diff);
				my %in_array2 = map { $_ => 1 } @tmp_sim;
				my @array3 = grep { $in_array2{$_} } @tmp_diff;
				if($#array3 >=0) {$prev_flag2=1; last;}
				#upto here
		}	
	}

	$sim1 = join("",@sim1);	
	$sim2 = join("",@sim2);
	$sim2 =~ s/ //g;	

	#print $sim1,"<<$sim2\n";
	#print $#sim1,"<<$#sim2\n";
	#print length($sim2),"#",$prev_flag1,"#",$prev_flag2,"<<\n";
	if($#sim1 >=0 && $#sim2 >=0 && length($sim2)>1 && $prev_flag1 !=1 && $prev_flag2 !=1 ) 
	{
		$match_flag1=0;
		$match_flag2=0;
		for($i=0; $i<=$#diff1; $i++)
		{
			my($e1,$e2) = split(/\|/,$diff1[$i]);
			@e1 = split(/\s+/,$e1);
			@e2 = split(/\s+/,$e2);
			my %in_array2 = map { $_ => 1 } @e2;
			my @array3 = grep { $in_array2{$_} } @e1;
			if($#array3 >=0) {$match_flag1=1; last;}
		}		
		for($i=0; $i<=$#diff2; $i++)
		{
			my($e1,$e2) = split(/\|/,$diff2[$i]);
			@e1 = split(/\s+/,$e1);
			@e2 = split(/\s+/,$e2);
			my %in_array2 = map { $_ => 1 } @e2;
			my @array3 = grep { $in_array2{$_} } @e1;
			if($#array3 >=0) {$match_flag2=1; last;}
		}		
	if($#diff1 == $#diff2 && $#diff1 == 0 && $match_flag1==0 && $match_flag2 == 0)
	{
		$ms_1 =~ s/\(.*\)/X1/;	
		$ms_2 =~ s/\(.*\)/X1/;
		my($token_1a,$token_1b) = split(/\|/,$diff1[0]);	
		my($token_2a,$token_2b) = split(/\|/,$diff2[0]);
		my($entry) = $diff1[0]."#".$diff2[0];
		$check_TT{$entry} = 1;
		#$atomic_TT{$token_1a} = $token_2a;
		#$atomic_TT{$token_1b} = $token_2b;
		if($atomic_TT{"$token_1a#$token_2a"} eq '')
		{
			$atomic_TT{"$token_1a#$token_2a"} = 1;
		}
		else
		{
			my $val = $atomic_TT{"$token_1a#$token_2a"};
			$val++;
			$atomic_TT{"$token_1a#$token_2a"} = $val;
		}
		if($atomic_TT{"$token_1b#$token_2b"} eq '')
		{
			$atomic_TT{"$token_1b#$token_2b"} = 1;
		}
		else
		{
			my $val = $atomic_TT{"$token_1b#$token_2b"};
			$val++;
			$atomic_TT{"$token_1b#$token_2b"} = $val;
		}
		$key = $ms_1."#".$ms_2;
		if($TT{$key} eq '' )
		{
			#print $h1,"\n",$h2,"\n",$key,"\n";
			$TT{$key} = 1;
			$LTT_count++;
		}
	}
	elsif( $#diff1 == $#diff2 && $match_flag1==0 && $match_flag2 == 0)
	{
		my(%map1,%map2);
		my($uncheck_count) = 0;
		for($i=0; $i<=$#diff1; $i++)
		{
			$flag = 0;
			for($j=0; $j<=$#diff2; $j++)
			{
				$tmp = $diff1[$i]."#".$diff2[$j];
				if($check_TT{$tmp} ne '') 
				{$flag = 1; $map1{$i}=$j;$map2{$j}=$i;last; }		
			}
			if($flag == 0) { $uncheck_count++;}
		}
		$count1 = keys %map1;
		$count2 = keys %map2;
	
		if($#diff1-$count1 == 0 && $#diff2-$count2 == 0)
		{
			for($i=0; $i<=$#diff1; $i++)
			{
				if($map1{$i} eq '') {last;}
			}				
			for($j=0; $j<=$#diff2; $j++)
			{
				if($map2{$j} eq '') {last;}
			}	
			my($token_1a,$token_1b) = split(/\|/,$diff1[$i]);	
			my($token_2a,$token_2b) = split(/\|/,$diff2[$j]);
			my($entry) = $diff1[$i]."#".$diff2[$j];
			$check_TT{$entry} = 1;
			#$atomic_TT{$token_1a} = $token_2a;
			#$atomic_TT{$token_1b} = $token_2b;
			if($atomic_TT{"$token_1a#$token_2a"} eq '')
			{
				$atomic_TT{"$token_1a#$token_2a"} = 1;
			}
			else
			{
				my $val = $atomic_TT{"$token_1a#$token_2a"};
				$val++;
				$atomic_TT{"$token_1a#$token_2a"} = $val;
			}
			if($atomic_TT{"$token_1b#$token_2b"} eq '')
			{
				$atomic_TT{"$token_1b#$token_2b"} = 1;
			}
			else
			{
				my $val = $atomic_TT{"$token_1b#$token_2b"};
				$val++;
				$atomic_TT{"$token_1b#$token_2b"} = $val;
			}
			#$atomic_TT{"$token_1a#$token_2a"} = 1;
			#$atomic_TT{"$token_1b#$token_2b"} = 1;
			$map2{$j} = $i;		
			$index=0;
			my($str1,$str2);
			for($i=0; $i<=$#token_1; $i++)
			{
				if( $token_1[$i] =~ /\(/)
				{
					my($sim,$diff) = split(/\(/,$token_1[$i]);
					$index++;
                    $str1 .= $sim."X"." ";
                    #santanu
				}
			}
			$index=0;
			my($str2);
			for($i=0; $i<=$#token_2; $i++)
			{
				if( $token_2[$i] =~ /\(/)
				{
					my($sim,$diff) = split(/\(/,$token_2[$i]);
					$mod_index = $map2{$index};
					$index++;
					$mod_index++;
					$str2 .= $sim."X"." ";
                    #santanu " "
				}
			}
			$key = $str1."#".$str2;
			if($TT{$key} eq '')
			{	
				#print $h1,"\n",$h2,"\n",$key,"\n";
				$TT{$key} = 2;
				$LTT_count++;
				#print $LTT_count,"*\n";
			}

		}
	}
	}
}


sub learn_DTTL
{
	my($ms_1, $ms_2) = @_;
	my($prev_flag1,$prev_flag2);	
	$h1 = $ms_1; $h2=$ms_2;

	$ms_1 =~ s/^\s+//g;
	$ms_2 =~ s/^\s+//g;
	$ms_1 =~ s/\s+$//g;
	$ms_2 =~ s/\s+$//g;
	my(@diff1,@diff2);
	my(@sim1,@sim2);	
	my(@arr1,@arr2);
	my($str1_a,$str1_b,$str2_a,$str2_b);	
	my @token_1 = split(/\)/,$ms_1);
	my @token_2 = split(/\)/,$ms_2);

	for($i=0; $i<=$#token_1; $i++)
	{
		$prev_flag1 = 0;
		if( $token_1[$i] =~ /\(/)
		{
			my($sim,$diff) = split(/\(/,$token_1[$i]);
			$sim =~ s/^\s+//;
			$sim =~ s/\s+$//;
			push(@diff1,$diff);
			if($sim ne '') {
				$tmp = "S:".$sim;
				push(@arr1,$tmp);
				push(@sim1,$sim);
				#this block checks if sim matches with pre diff
				my @tmp_sim = split(/\s+/,$sim);
				my $tmp_diff = join("",@diff1);
				$tmp_diff =~ s/\|/ /g;
				my @tmp_diff = split(/\s+/,$tmp_diff);
				my %in_array2 = map { $_ => 1 } @tmp_sim;
				my @array3 = grep { $in_array2{$_} } @tmp_diff;
				if($#array3 >=0) {$prev_flag1=1; last;}
				#upto here
			}
				$tmp = "D:".$diff;
				push(@arr1,$tmp);
		}
		else
		{
			$token_1[$i] =~ s/^\s+//;
			$token_1[$i] =~ s/\s+$//;
			$tmp = "S:".$token_1[$i];
			push(@arr1,$tmp);
			push(@sim1,$token_1[$i]);
				#this block checks if sim matches with pre diff
				my @tmp_sim = split(/\s+/,$token_1[$i]);
				my $tmp_diff = join("",@diff1);
				$tmp_diff =~ s/\|/ /g;
				my @tmp_diff = split(/\s+/,$tmp_diff);
				my %in_array2 = map { $_ => 1 } @tmp_sim;
				my @array3 = grep { $in_array2{$_} } @tmp_diff;
				if($#array3 >=0) {$prev_flag1=1; last;}
				#upto here
		}	
	}
	#print $prev_flag1,"..........$prev_flag2...MAAAAAAAAA\n";
	for($i=0; $i<=$#token_2; $i++)
	{
		$prev_flag2 = 0;
		if( $token_2[$i] =~ /\(/)
		{
			my($sim,$diff) = split(/\(/,$token_2[$i]);
			$sim =~ s/^\s+//;
			$sim =~ s/\s+$//;
			push(@diff2,$diff);
			if($sim ne '') {
				$tmp = "S:".$sim;
				push(@arr2,$tmp);
				push(@sim2,$sim);
				#this block checks if sim matches with pre diff
				my @tmp_sim = split(/\s+/,$sim);
				my $tmp_diff = join("",@diff2);
				$tmp_diff =~ s/\|/ /g;
				my @tmp_diff = split(/\s+/,$tmp_diff);
				my %in_array2 = map { $_ => 1 } @tmp_sim;
				my @array3 = grep { $in_array2{$_} } @tmp_diff;
				if($#array3 >=0) {$prev_flag2=1; last;}
				#upto here
			}
				$tmp = "D:".$diff;
				push(@arr2,$tmp);
		}
		else
		{
			$token_1[$i] =~ s/^\s+//;
			$token_1[$i] =~ s/\s+$//;
			$tmp = "S:".$token_2[$i];
			push(@arr2,$tmp);
			push(@sim2,$token_2[$i]);
				#this block checks if sim matches with pre diff
				my @tmp_sim = split(/\s+/,$sim);
				my $tmp_diff = join("",@diff2);
				$tmp_diff =~ s/\|/ /g;
				my @tmp_diff = split(/\s+/,$tmp_diff);
				my %in_array2 = map { $_ => 1 } @tmp_sim;
				my @array3 = grep { $in_array2{$_} } @tmp_diff;
				if($#array3 >=0) {$prev_flag2=1; last;}
				#upto here
		}	
	}

	$sim1 = join("",@sim1);	
	$sim2 = join("",@sim2);	
	$sim2 =~ s/ //g;	
#	print @arr1,"\n";
#	print "@arr2\n";
#	print $prev_flag1,"..........$prev_flag2...MAA\n";

	if($#sim1 >=0 && $#sim2 >=0 && length($sim2)>1  && $prev_flag1 != 1 && $prev_flag2 != 1) 
	{	
		$match_flag1=0;
		$match_flag2=0;
		for($i=0; $i<=$#diff1; $i++)
		{
			my($e1,$e2) = split(/\|/,$diff1[$i]);
			@e1 = split(/\s+/,$e1);
			@e2 = split(/\s+/,$e2);
			my %in_array2 = map { $_ => 1 } @e2;
			my @array3 = grep { $in_array2{$_} } @e1;
			if($#array3 >=0) {$match_flag1=1; last;}
		}		
		for($i=0; $i<=$#diff2; $i++)
		{
			my($e1,$e2) = split(/\|/,$diff2[$i]);
			@e1 = split(/\s+/,$e1);
			@e2 = split(/\s+/,$e2);
			my %in_array2 = map { $_ => 1 } @e2;
			my @array3 = grep { $in_array2{$_} } @e1;
			if($#array3 >=0) {$match_flag2=1; last;}
		}		
		if($#sim1 == $#sim2 && $#sim1 == 0 && $match_flag1==0 && $match_flag2 ==0 )
		{
			for($i=0; $i<=$#arr1; $i++)
			{
				if($arr1[$i] =~ /^S:/)
				{
					$str1_a .= "X1 ";
					$str1_b .= "X1 ";
					$src = $arr1[$i];
				}
				else
				{
					$arr1[$i] =~ s/^D://;
					my($first,$scnd) = split(/\|/,$arr1[$i]);
					$str1_a .= $first." ";
					$str1_b .= $scnd." ";
				}
			}
			for($i=0; $i<=$#arr2; $i++)
			{
				if($arr2[$i] =~ /^S:/)
				{
					$str2_a .= "X1 ";
					$str2_b .= "X1 ";
					$tgt = $arr2[$i];
				}
				else
				{
					$arr2[$i] =~ s/^D://;
					my($first,$scnd) = split(/\|/,$arr2[$i]);
					$str2_a .= $first." ";
					$str2_b .= $scnd." ";
				}
			}
	
	
			$key1 = $str1_a."#".$str2_a;	
			$key2 = $str1_b."#".$str2_b;
			#print $ms_1,"\n",$ms_2,"\n",$key1,"\n",$key2,"\n";	
			if($TT{$key1} eq '' )
			{
				$TT{$key1} = 1;
				#print $h1,"\n",$h2,"\n",$key1,"\n";
			}
			else
			{
				my $val = $TT{$key1};
				$val++;
				$TT{$key1} = $val;
			}
			if($TT{$key2} eq '' )
			{
				$TT{$key2} = 1;
				$DTT_count++;
				#print $h1,"\n",$h2,"\n",$key2,"\n";
				#print $DTT_count,"#\n";
			}
			else
			{
				my $val = $TT{$key2};
				$val++;
				$TT{$key2} = $val;
			}
			
			$src =~ s/^S://;
			$tgt =~ s/^S://;
			my($entry) = $src."#".$tgt;
			$check_TT{$entry} = 1;
			#$atomic_TT{$src} = $tgt;
			if($atomic_TT{"$src#$tgt"} eq '')
			{
				$atomic_TT{"$src#$tgt"} = 1;
			}
			else
			{
				my $val = $atomic_TT{"$src#$tgt"};
				$val++;
				$atomic_TT{"$src#$tgt"} = $val;
			}
		}
	
		elsif( $#sim1 == $#sim2 && $match_flag==0 && $match_flag==0 )
		{
			my(%map1,%map2);
			my($uncheck_count) = 0;
			for($i=0; $i<=$#sim1; $i++)
			{
				$flag = 0;
				for($j=0; $j<=$#sim2; $j++)
				{
					$tmp = $sim1[$i]."#".$sim2[$j];
					if($check_TT{$tmp} ne '') 
					{$flag = 1; $map1{$i}=$j;$map2{$j}=$i;last; }		
				}
				if($flag == 0) { $uncheck_count++;}
			}
			$count1 = keys %map1;
			$count2 = keys %map2;
		
			if($#sim1-$count1 == 0 && $#sim2-$count2 == 0)
			{
				for($i=0; $i<=$#sim1; $i++)
				{
					if($map1{$i} eq '') {last;}
				}				
				for($j=0; $j<=$#sim2; $j++)
				{
					if($map2{$j} eq '') {last;}
				}
				my $src = $sim1[$i];	
				my $tgt = $sim2[$j];	
				my($entry) = $src."#".$tgt;
				$check_TT{$entry} = 1;
				#$atomic_TT{$src} = $tgt;
			#	$atomic_TT{"$src#$tgt"} = 1;
                        	if($atomic_TT{"$src#$tgt"} eq '')
                        	{
                                	$atomic_TT{"$src#$tgt"} = 1;
                       		}
                        	else
                        	{
                                	my $val = $atomic_TT{"$src#$tgt"};
					$val++;
                                	$atomic_TT{"$src#$tgt"} = $val;
                       	 	}

				$map2{$j} = $i;		
				$index=0;
				my($str1,$str2);  ###upto here done

				for($i=0; $i<=$#arr1;$i++)
				{
					if($arr1[$i] =~ /^S:/)
					{
						$index++;
						$str1_a .= "X".$index." ";
						$str1_b .= "X".$index." ";
					}
					else
					{
						$arr1[$i] =~ s/^D://;
						my($first,$scnd) = split(/\|/,$arr1[$i]);
						$str1_a .= $first." ";
						$str1_b .= $scnd." ";
					}
				}
				$index = 0;
				for($i=0; $i<=$#arr2;$i++)
				{
					if($arr2[$i] =~ /^S:/)
					{
						$mod_index = $map2{$index};
						$mod_index++;
						$index++;
						$str2_a .= "X".$mod_index." ";
						$str2_b .= "X".$mod_index." ";
					}
					else
					{
						$arr2[$i] =~ s/^D://;
						my($first,$scnd) = split(/\|/,$arr2[$i]);
						$str2_a .= $first." ";
						$str2_b .= $scnd." ";
					}
				}


	                        $key1 = $str1_a."#".$str2_a;
        	                $key2 = $str1_b."#".$str2_b;
				#print $ms_1,"\n",$ms_2,"\n";
				#print $key1,"\n",$key2,"\n";
				#print $ms_1,"\n",$ms_2,"\n",$key1,"\n",$key2,"<<\n";	
                	        if($TT{$key1} eq '' )
                       		{
                                	$TT{$key1} = 1;
					#print $h1,"\n",$h2,"\n",$key1,"\n";
                        	}
				else
				{
					my $val = $TT{$key1};
					$val++;
					$TT{$key1} = $val;
				}
	                        if($TT{$key2} eq '' )
        	                {
                	                $TT{$key2} = 1;
	                       	        $DTT_count++;
					#print $h1,"\n",$h2,"\n",$key2,"\n";
					#print $DTT_count,"#\n";
        	                }
				else
				{
					my $val = $TT{$key2};
					$val++;
					$TT{$key2} = $val;
				}
			}
		}
	}
}


sub MS_STTL 
{
	my($ref_arr1,$ref_arr2) = @_;
	my %mat;
	@arr1 = @{$ref_arr1}; 
	@arr2 = @{$ref_arr2};
	
	$len1 = $#arr1+1;
	$len2 = $#arr2+1;

	for (my $i = 0; $i <= $len1; ++$i)
	{
		$mat{$i}{0} = $i;
	}
	for (my $j = 0; $j <= $len2; ++$j)
	{
		$mat{0}{$j} = $j;
	}
=c
	for (my $i = 0; $i <= $len1; ++$i)
	{
		for (my $j = 0; $j <= $len2; ++$j)
		{
			$mat{$i}{$j} = 0;
			$mat{0}{$j} = $j;
		}
		$mat{$i}{0} = $i;
	}
=cut

	for (my $i = 1; $i <= $len1; ++$i)
	{
		for (my $j = 1; $j <= $len2; ++$j)
		{
			my $cost = ($arr1[$i-1] eq $arr2[$j-1]) ? 0 : 1;
				$mat{$i}{$j} = min([$mat{$i-1}{$j} + 1,
				$mat{$i}{$j-1} + 1,
				$mat{$i-1}{$j-1} + $cost]);
		}
	}
	
	my ($m_seq) = traceBack('','','',\%mat,$len1,$len2,\@arr1,\@arr2,'');
	my ($new_m_seq) = '';
	@token = split(/\s+/,$m_seq);
	for($i=0; $i<=$#token; $i++)
	{
		$cur = $token[$i];
		$next = $token[$i+1];
		$prev = $token[$i-1];
		if($cur =~ /\((.*)\)/)
		{
			$item = $1;
			if($next =~ /\((.*)\)/)
			{
				my($a,$b) = split(/\|/,$item);
				$tmp1  .= $a." "; 
				$tmp2  .= $b." "; 
			}
			else	
			{
				my($a,$b) = split(/\|/,$item);
				$tmp1  .= $a; 
				$tmp2  .= $b;
				$tmp1 =~ s/null\s+//g; 
				$tmp2 =~ s/null\s+//g; 
				$new_m_seq .= "(".$tmp1."|".$tmp2.") ";
				$tmp1=""; 
				$tmp2=""; 
			}
		}
		else
		{
			$new_m_seq .= $cur." ";
		} 
		
	}
#	print $new_m_seq,".........\n";
#	print $m_seq,".........\n";	
	return $new_m_seq; 
}


sub traceBack
{
	#my($seq);
	my($row1,$row2,$row3,$hashref,$i,$j,$ref1,$ref2,$seq) = @_;
	%ed = %{$hashref};
	@ar1 = @{$ref1};
	@ar2 = @{$ref2};
	my($diag);
	if($i > 0 && $j>0 )
	{
		$flag = 0;
		$diag = $ed{$i-1}{$j-1}; $diagCh = "|";
		if($ar1[$i-1] ne $ar2[$j-1]) { $diag++; $diagCh = ' '; $flag = 1}
		if($ed{$i}{$j} == $diag)
		{
			if($flag == 0){
				traceBack($ar1[$i-1].$row1,$diagCh.$row2,$ar2[$j-1].$row3,\%ed,($i-1),($j-1),\@ar1,\@ar2,$ar1[$i-1]." ".$seq);
			}
			else {
				traceBack($ar1[$i-1].$row1,$diagCh.$row2,$ar2[$j-1].$row3,\%ed,($i-1),($j-1),\@ar1,\@ar2,"(".$ar1[$i-1]."|".$ar2[$j-1].") ".$seq);
			}
			
		}
		elsif($ed{$i}{$j} == $ed{$i-1}{$j}+1)
		{
			traceBack($ar1[$i-1].$row1," ".$row2,"-".$row3,\%ed,$i-1,$j,\@ar1,\@ar2,"(".$ar1[$i-1]."|null) ".$seq);
		}
		else
		{
			traceBack("-".$row1," ".$row2,$ar2[$j-1].$row3,\%ed,$i,$j-1,\@ar1,\@ar2,"(null|".$ar2[$j-1].") ".$seq);
		}
		
	}
	elsif($i > 0)
	{
		traceBack($ar1[$i-1].$row1," ".$row2,"-".$row3,\%ed,$i-1,$j,\@arr1,\@ar2,"(".$ar1[$i-1]."|null) ".$seq);
	}		
	elsif($j > 0)
	{
		traceBack("-".$row1," ".$row2,$ar2[$j-1].$row3,\%ed,$i,$j-1,\@ar1,\@ar2,"(null|".$ar2[$j-1].") ".$seq);
	}
	else
	{
		return $seq;
	}		
}

sub min
{
    my @list = @{$_[0]};
    my $min = $list[0];

    foreach my $i (@list)
    {
        $min = $i if ($i < $min);
    }

    return $min;
}

