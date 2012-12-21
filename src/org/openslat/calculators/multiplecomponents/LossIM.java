package org.openslat.calculators.multiplecomponents;

import java.util.ArrayList;
import org.openslat.calculators.collapse.LossCollapse;
import org.openslat.calculators.component.LossIMNC;
import org.openslat.model.structure.Component;
import org.openslat.options.CalculationOptions;

/**
 * @author alan
 * 
 */
public class LossIM {

	private CalculationOptions calculationOptions;
	private LossIMNC lossIMNC;
	private COVLkLmIM covLkLmIM;

	public double meanLoss(double im) {
		ArrayList<Component> components = calculationOptions.getStructure()
				.getComponents();

		// compute mean loss (no collapse)
		double lossNC = 0;
		double lossC = 0;
		for (Component c : components) {
			lossNC = lossNC + lossIMNC.meanLoss(c, im);
		}

		// COMPUTE LOSS taking col/no col into account
		double probCollapse;
		if (calculationOptions.isCollapse()) {
			probCollapse = calculationOptions.getStructure().getPc().getPcim()
					.probability(im);
		} else {
			probCollapse = 0;
		}

		LossCollapse lossCollapse = new LossCollapse();
		lossCollapse.setCalculationOptions(calculationOptions);
		lossC = lossCollapse.meanLoss();
		double totalLoss = lossNC * (1 - probCollapse) + lossC * (probCollapse);
		return totalLoss;
	}

	public double meanLossNC(double im) {
		ArrayList<Component> components = calculationOptions.getStructure()
				.getComponents();

		// compute mean loss (no collapse)
		double lossNC = 0;
		double lossC = 0;
		for (Component c : components) {
			lossNC = lossNC + lossIMNC.meanLoss(c, im);
		}

		double probCollapse;
		probCollapse = 0;

		LossCollapse lossCollapse = new LossCollapse();
		lossCollapse.setCalculationOptions(calculationOptions);
		lossC = lossCollapse.meanLoss();
		double totalLoss = lossNC * (1 - probCollapse) + lossC * (probCollapse);
		return totalLoss;
	}

	public double sigmaLoss(double im) {
		ArrayList<Component> components = calculationOptions.getStructure()
				.getComponents();
		// TODO: return sigma (as coded...?) or variance?

		// compute mean loss (no collapse)
		double lossNC = 0;
		double lossC = 0;
		for (Component c : components) {
			lossNC = lossNC + lossIMNC.meanLoss(c, im);
		}

		// compute variance (no collapse)
		double varLossNC = 0;
		for (int k = 0; k < components.size(); ++k) {
			for (int m = 0; m <= k; ++m) {
				double cov_LIMNCkm = covLkLmIM.covLIMNCkm(components.get(k),
						components.get(m), im);
				if (k == m) {
					varLossNC = varLossNC + cov_LIMNCkm;
				} else {
					varLossNC = varLossNC + 2 * cov_LIMNCkm;
				}
			}
		}

		// convert variance back to dispersion
		// double sigmaLossNC = Math.sqrt(Math.log(varLossNC / Math.pow(lossNC,
		// 2)
		// + 1.0));

		// COMPUTE LOSS taking col/no col into account
		double probCollapse;
		if (calculationOptions.isCollapse()) {
			probCollapse = calculationOptions.getStructure().getPc().getPcim()
					.probability(im);
		} else {
			probCollapse = 0;
		}

		LossCollapse lossCollapse = new LossCollapse();
		lossCollapse.setCalculationOptions(calculationOptions);
		lossC = lossCollapse.meanLoss();
		double totalLoss = lossNC * (1 - probCollapse) + lossC * (probCollapse);

		double varTotalLossNC = Math.pow(lossNC, 2)
				* (Math.exp(Math.pow(lossNC, 2) - 1));
		double varTLossC = Math.pow(lossC, 2)
				* (Math.exp(Math.pow(lossC, 2) - 1));

		double varTotalLoss = varTotalLossNC * (1.0 - probCollapse) + varTLossC
				* probCollapse + Math.pow((lossNC - totalLoss), 2)
				* (1.0 - probCollapse) + Math.pow((lossC - totalLoss), 2)
				* probCollapse;

		// convert variance back to dispersion
		double sigmaTotalLoss = Math.sqrt(Math.log(varTotalLoss
				/ Math.pow(totalLoss, 2) + 1.0));
		return sigmaTotalLoss;
	}
}