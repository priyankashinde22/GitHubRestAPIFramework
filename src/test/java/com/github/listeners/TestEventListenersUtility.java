package com.github.listeners;


import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.github.utils.ExtentReportsUtility;

public class TestEventListenersUtility implements ITestListener {
	private static ExtentReportsUtility extentUtilityObject;

	@Override
	public void onTestStart(ITestResult result) { // before every @Test method called this method is executed
		extentUtilityObject.startSingleTestReport(result.getMethod().getMethodName());	
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		extentUtilityObject.logTestpassed(result.getMethod().getMethodName()+" is passed");	
	}

	@Override
	public void onTestFailure(ITestResult result) {
		extentUtilityObject.logTestFailed(result.getMethod().getMethodName()+" is failed");
		extentUtilityObject.logTestFailedWithException(result.getThrowable());
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		
	}

	@Override
	public void onStart(ITestContext context) {
		extentUtilityObject=ExtentReportsUtility.getInstance();
		System.out.println("report utility object created="+extentUtilityObject.toString());
		extentUtilityObject.startExtentReport();
	}

	@Override
	public void onFinish(ITestContext context) {
		extentUtilityObject.endReport();
	}
	
}
