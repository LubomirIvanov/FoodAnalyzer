import command.CommandProcessor;
import constants.CommandProcessorTestConstants;
import database.PseudoDatabase;
import decoder.ImageDecoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

public class CommandProcessorTest {
    private static PseudoDatabase pseudoDatabase;
    private static CommandProcessor commandProcessor;

    private static String raffaelloString;
    private static String raffaelloLocalString;
    private static String raffaelloDescriptionString;
    private static String raffaelloDescriptionLocalString;

    @BeforeClass
    public static void initialize() {
        pseudoDatabase = PseudoDatabase.getInstance();
        commandProcessor = new CommandProcessor();
        File raffaello = new File("src/foodInfos/raffaello");
        File raffaelloLocal = new File("src/foodInfos/raffaelloLocal");

        File raffaelloDescription = new File("src/foodInfos/raffaelloDescription");
        File raffaelloDescriptionLocal = new File("src/foodInfos/raffaelloDescriptionLocal");


        try (InputStream stream = new FileInputStream(raffaello);
             InputStream secondStream = new FileInputStream(raffaelloLocal);
             InputStream descriptionStream = new FileInputStream(raffaelloDescription);
             InputStream descriptionLocalStream = new FileInputStream(raffaelloDescriptionLocal)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            raffaelloString = reader
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));

            BufferedReader secondReader = new BufferedReader(new InputStreamReader(secondStream));
            raffaelloLocalString = secondReader
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));

            BufferedReader descriptionReader = new BufferedReader(new InputStreamReader(descriptionStream));
            raffaelloDescriptionString = descriptionReader
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));

            BufferedReader descriptionLocalReader = new BufferedReader(new InputStreamReader(descriptionLocalStream));
            raffaelloDescriptionLocalString = descriptionLocalReader
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void clearDatabase() {
        pseudoDatabase.clear();
    }

    @Test
    public void testNullInputShouldResultInErrorMessage() throws URISyntaxException {
        String result = commandProcessor
                .processCommand(null);

        Assert.assertEquals(CommandProcessorTestConstants.NULL_INPUT_MESSAGE, result);
    }

    @Test
    public void testWrongCommandShouldResultInErrorMessage() throws URISyntaxException {
        String result = commandProcessor
                .processCommand(CommandProcessorTestConstants.WRONG_COMMAND);

        Assert.assertEquals(CommandProcessorTestConstants.WRONG_COMMAND_MESSAGE, result);
    }

    @Test
    public void testNullParametersShouldResultInErrorMessage() throws URISyntaxException {
        String result = commandProcessor
                .processCommand(CommandProcessorTestConstants.NULL_PARAMETERS_COMMAND);

        Assert.assertEquals(CommandProcessorTestConstants.WRONG_COMMAND_MESSAGE, result);
    }

    @Test
    public void testGeneralCommandShouldReturnRightResult() throws URISyntaxException {
        String result = commandProcessor
                .processCommand(CommandProcessorTestConstants.GENERAL_VALID_COMMAND);

        Assert.assertEquals(raffaelloString, result);
    }

    @Test
    public void testGeneralCommandShouldReturnRightResultForLocal() throws URISyntaxException {
        commandProcessor
                .processCommand(CommandProcessorTestConstants.GENERAL_VALID_COMMAND);
        String result = commandProcessor
                .processCommand(CommandProcessorTestConstants.GENERAL_VALID_COMMAND);

        Assert.assertEquals(raffaelloLocalString, result);
    }

    @Test
    public void testBarcodeCommandShouldReturnRightResult() throws URISyntaxException {
        commandProcessor
                .processCommand(CommandProcessorTestConstants.GENERAL_VALID_COMMAND);
        String result = commandProcessor
                .processCommand(CommandProcessorTestConstants.BARCODE_VALID_COMMAND);

        Assert.assertEquals(raffaelloString, result);
    }

    @Test
    public void testBarcodeCommandShouldReturnErrorMessage() throws URISyntaxException {
        String result = commandProcessor
                .processCommand(CommandProcessorTestConstants.BARCODE_VALID_COMMAND);

        Assert.assertEquals(CommandProcessorTestConstants.NON_EXISTING_BARCODE_MESSAGE, result);
    }

    @Test
    public void testDescriptionCommandShouldReturnRightResult() throws URISyntaxException {
        String result = commandProcessor
                .processCommand(CommandProcessorTestConstants.DESCRIPTION_VALID_COMMAND);

        Assert.assertEquals(raffaelloDescriptionString, result);
    }

    @Test
    public void testDescriptionLocalCommandShouldReturnRightResult() throws URISyntaxException {
        commandProcessor
                .processCommand(CommandProcessorTestConstants.DESCRIPTION_VALID_COMMAND);
        String result = commandProcessor
                .processCommand(CommandProcessorTestConstants.DESCRIPTION_VALID_COMMAND);

        Assert.assertEquals(raffaelloDescriptionLocalString, result);
    }

    @Test
    public void testBarcodeCommandByImageShouldReturnRightResult() throws URISyntaxException {
        commandProcessor
                .processCommand(CommandProcessorTestConstants.GENERAL_VALID_COMMAND);
        String result = commandProcessor
                .processCommand(CommandProcessorTestConstants.BARCODE_BY_IMAGE_COMMAND);

        Assert.assertEquals(raffaelloString, result);
    }

    @Test
    public void testBarcodeCommandByImageShouldReturnErrorMessage() throws URISyntaxException {
        commandProcessor
                .processCommand(CommandProcessorTestConstants.GENERAL_VALID_COMMAND);
        String result = commandProcessor
                .processCommand(CommandProcessorTestConstants.NON_EXISTING_BARCODE_BY_IMAGE_COMMAND);

        Assert.assertEquals(CommandProcessorTestConstants.NON_EXISTING_BARCODE_MESSAGE, result);
    }
    @Test
    public void testBarcodeCommandByImageWhichDoesntExistShouldReturnErrorMessage() throws URISyntaxException {

        String result = commandProcessor
                .processCommand(CommandProcessorTestConstants.NON_EXISTING_IMAGE_COMMAND);

        Assert.assertEquals(ImageDecoder.ERROR_MESSAGE, result);
    }


}
