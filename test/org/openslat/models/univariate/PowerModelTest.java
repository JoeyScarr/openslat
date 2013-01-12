package org.openslat.models.univariate;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PowerModelTest {

	double[] testValues = { 0, 2 };
	double[] values = { -1.4142, 0, 1.4142, -1, 0, 1, -0.17678, 0, 0.17678 };
	double[] derivatives = {};

	PowerModel pm1;
	PowerModel pm2;
	PowerModel pm3;

	@Before
	public void setUp() throws Exception {

		pm1 = new PowerModel();
		double[] pparams1 = { 0.5, 1.5 };
		pm1.constructPowerModel(pparams1);

		pm2 = new PowerModel();
		double[] pparams2 = { 0.5, 0.0 };
		pm2.constructPowerModel(pparams2);

		pm3 = new PowerModel();
		double[] pparams3 = { 0.5, -1.5 };
		pm3.constructPowerModel(pparams3);
	}
	
	@Test
	public void testConstructor(){
		assertEquals(0.5,pm1.getParameters()[0],0.01);
		assertEquals(1.5,pm1.getParameters()[1],0.01);
	}
	
	@Test
	public void test1() {
		assertEquals(0, pm1.value(0), 0.001);
		assertEquals(1.4142, pm1.value(2), 0.001);
	}

	@Test
	public void test2() {
		assertEquals(0.5, pm2.value(1), 0.001);
		assertEquals(0.5, pm2.value(2), 0.001);
	}

	@Test
	public void test3() {
		assertEquals(0.5, pm3.value(0), 0.001);
		assertEquals(0.1768, pm3.value(2), 0.001);
	}
}
