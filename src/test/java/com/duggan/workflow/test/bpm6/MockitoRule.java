package com.duggan.workflow.test.bpm6;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.mockito.MockitoAnnotations;

public class MockitoRule extends TestWatcher {
    private boolean mockInitialized = false;

    @Override
    protected void starting(Description d) {
        if (!mockInitialized) {
            MockitoAnnotations.initMocks(this);
            mockInitialized = true;  
        }
    }
}