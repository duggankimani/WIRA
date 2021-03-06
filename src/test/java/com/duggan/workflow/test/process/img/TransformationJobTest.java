package com.duggan.workflow.test.process.img;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ilesteban.processimage.ProcessImageProcessor;
import com.ilesteban.processimage.ProcessImageProcessorConfiguration;
import com.ilesteban.processimage.transformation.AttributeNameTransformationJob;
import com.ilesteban.processimage.transformation.TaskBorderTransformationJob;
import com.ilesteban.processimage.transformation.UndefinedValueForAttributeTransformationJob;

/**
 *
 * @author esteban
 */
public class TransformationJobTest {

    @BeforeClass
    public static void setup() throws IOException {
    }

    @Test
    public void testUndefinedValueForAttributeTransformationJob() throws Exception {
        //Create a new Configuration object to set the padd to use in the label of the tasks
        ProcessImageProcessorConfiguration config = new ProcessImageProcessorConfiguration();
        config.setDefaultextPad(20.0f);

        //Create a processor instance for UndefinedValueForAttributeTransformationJobTest.svg
        ProcessImageProcessor processor = new ProcessImageProcessor(SimpleTest.class.getResourceAsStream("/UndefinedValueForAttributeTransformationJobTest.svg"), config);

        //'undefined' is not a valid value for font-size
        UndefinedValueForAttributeTransformationJob job = new UndefinedValueForAttributeTransformationJob();
        job.addAttributeToProcess("text", "font-size", "20");
        processor.addTransformationJob(job);
        
        processor.applyTransformationJobs(true);
    
        this.toTmpFile(processor.toPNG(), "modified", ".png");
        
    }
    
    @Test
    public void testAttributeNameTransformationJob() throws Exception {
        //Create a new Configuration object to set the padd to use in the label of the tasks
        ProcessImageProcessorConfiguration config = new ProcessImageProcessorConfiguration();
        config.setDefaultextPad(20.0f);

        //Create a processor instance for AttributeNameTransformationJobTest.svg
        ProcessImageProcessor processor = new ProcessImageProcessor(SimpleTest.class.getResourceAsStream("/AttributeNameTransformationJobTest.svg"), config);

        //<image> link 'href' should be 'xlink:href'
        AttributeNameTransformationJob job = new AttributeNameTransformationJob();
        job.addAttributeToProcess("image", "href", "xlink:href");
        processor.addTransformationJob(job);
        
        processor.applyTransformationJobs(true);
    
        this.toTmpFile(processor.toPNG(), "modified", ".png");
        
    }
    
    @Test
    public void testTaskBorderTransformationJob() throws Exception{

        //Create a new Configuration object to set the padd to use in the label of the tasks
        ProcessImageProcessorConfiguration config = new ProcessImageProcessorConfiguration();
        config.setDefaultextPad(20.0f);
        
        //Create a processor instance for test1.svg
        ProcessImageProcessor processor = new ProcessImageProcessor(SimpleTest.class.getResourceAsStream("/test1.svg"), config);
        
        //change some borders
        processor.addTransformationJob(new TaskBorderTransformationJob("Assign Bed", "#cccccc", 4));
        processor.addTransformationJob(new TaskBorderTransformationJob("Coordinate Staff", "#00ff00"));
        
        //apply the transformations
        processor.applyTransformationJobs(true);

        //write the modified file to disk
        this.toTmpFile(processor.toXML(), "modified", ".svg");
        this.toTmpFile(processor.toPNG(), "modified", ".png");
        
    }
    
    private File toTmpFile(Reader reader, String prefix, String suffix) throws IOException{
        return this.toTmpFile(new ReaderInputStream(reader), prefix, suffix);
    }
    
    private File toTmpFile(InputStream is, String prefix, String suffix) throws IOException{
        File tmpFile = File.createTempFile(prefix, suffix);
        
        IOUtils.copy(is, new FileOutputStream(tmpFile));
        
        return tmpFile;
    }
    
}