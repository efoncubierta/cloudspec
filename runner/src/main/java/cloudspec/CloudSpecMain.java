package cloudspec;

import cloudspec.spec.CloudSpec;

import java.io.FileInputStream;
import java.io.InputStream;

public class CloudSpecMain {

    public static void main(String[] args) throws Exception {

        // prepare input from argument or stream
        String inputFile = null;
        if (args.length > 0) inputFile = args[0];
        InputStream is = System.in;
        if (inputFile != null) is = new FileInputStream(inputFile);

        // create loader
        CloudSpecLoader loader = new CloudSpecLoader();

        // load spec
        CloudSpec spec = loader.load(is);

        // run spec
        CloudSpecValidator runner = DaggerCloudSpecRunnerComponent.create().buildCloudSpecRunner();
        runner.validate(spec);
    }
}
