/*
 * TaskTest.java
 * Copyright (C) 2011-2012  KIRC
 * 
 * This file is part of JEIRA.
 * 
 * JEIRA is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software 
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 * 
 * JEIRA is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
 * details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this software; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package se.kirc.geisa;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import se.kirc.geisa.data.plink.AffectionStatus;
import se.kirc.geisa.data.plink.Allele;
import se.kirc.geisa.data.plink.Genotype;

public class TaskTest {
	@Test
	public void testAlleleSummary() throws Exception {
		List<Genotype> genotypes = new ArrayList<Genotype>();
		genotypes.add(Genotype.HOMOZYGOTE_PRIMARY);
		genotypes.add(Genotype.HETEROZYGOTE);
		genotypes.add(Genotype.HOMOZYGOTE_SECONDARY);
		genotypes.add(Genotype.HOMOZYGOTE_SECONDARY);
		genotypes.add(Genotype.HETEROZYGOTE);
		genotypes.add(Genotype.HOMOZYGOTE_SECONDARY);
		genotypes.add(Genotype.HETEROZYGOTE);
		genotypes.add(Genotype.HOMOZYGOTE_SECONDARY);
		genotypes.add(Genotype.HETEROZYGOTE);
		genotypes.add(Genotype.HETEROZYGOTE);

		List<AffectionStatus> status = new ArrayList<AffectionStatus>();
		status.add(AffectionStatus.AFFECTED);
		status.add(AffectionStatus.AFFECTED);
		status.add(AffectionStatus.AFFECTED);
		status.add(AffectionStatus.AFFECTED);
		status.add(AffectionStatus.UNAFFECTED);
		status.add(AffectionStatus.AFFECTED);
		status.add(AffectionStatus.UNAFFECTED);
		status.add(AffectionStatus.UNAFFECTED);
		status.add(AffectionStatus.UNAFFECTED);
		status.add(AffectionStatus.UNAFFECTED);

//		TaskConfiguration configuration = new TaskConfiguration(genotypes,
//				status, Allele.A, Allele.G);
//		List<TaskConfiguration> configurations = new LinkedList<TaskConfiguration>();
//		configurations.add(configuration);
//		
//		Task task = new Task(configurations);
//		
//		System.out.println(task.call());
	}
}
