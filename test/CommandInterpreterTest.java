import command.CommandInterpreter;
import constants.CommandInterpreterTestConstants;
import enums.CommandType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CommandInterpreterTest {

    private static CommandInterpreter commandInterpreter;

    @BeforeClass
    public static void initialize() {
        commandInterpreter = new CommandInterpreter();
    }

    @Test
    public void testGetCommandTypeWithMissingParameterGeneralInfo() {
        CommandType result = commandInterpreter
                .getCommandType(CommandInterpreterTestConstants.MISSING_PARAMETER_GENERAL_INFO_COMMAND);
        Assert.assertNull(result);
    }

    @Test
    public void testGetCommandTypeWithMissingParameterBarcodeInfo() {
        CommandType result = commandInterpreter
                .getCommandType(CommandInterpreterTestConstants.MISSING_PARAMETER_BARCODE_INFO_COMMAND);
        Assert.assertNull(result);
    }

    @Test
    public void testGetCommandTypeWithMissingParameterIMGInfo() {
        CommandType result = commandInterpreter
                .getCommandType(CommandInterpreterTestConstants.MISSING_PARAMETER_IMG_INFO_COMMAND);
        Assert.assertNull(result);
    }

    @Test
    public void testGetCommandTypeWithBothParametersInvalid() {
        CommandType result = commandInterpreter
                .getCommandType(CommandInterpreterTestConstants.MISSING_BOTH_PARAMETERS_COMMAND);
        Assert.assertNull(result);
    }

    @Test
    public void testGetCommandTypeWithWrongCommand() {
        CommandType result = commandInterpreter
                .getCommandType(CommandInterpreterTestConstants.INVALID_COMMAND);
        Assert.assertNull(result);
    }

    @Test
    public void testGetCommandTypeWithValidGeneralInfoInput() {
        CommandType result = commandInterpreter
                .getCommandType(CommandInterpreterTestConstants.GENERAL_INFO_VALID_COMMAND);
        Assert.assertEquals(result, CommandType.GENERAL);
    }

    @Test
    public void testGetCommandTypeWithValidDescriptionInfoInput() {
        CommandType result = commandInterpreter
                .getCommandType(CommandInterpreterTestConstants.DESCRIPTION_INFO_VALID_COMMAND);
        Assert.assertEquals(result, CommandType.DESCRIPTION);
    }

    @Test
    public void testGetCommandTypeWithValidBarcodeInfoInput() {
        CommandType result = commandInterpreter
                .getCommandType(CommandInterpreterTestConstants.BARCODE_INFO_ONLY_VALID_COMMAND);
        Assert.assertEquals(result, CommandType.BARCODE);
    }

    @Test
    public void testGetCommandTypeWithValidIMGInfoInput() {
        CommandType result = commandInterpreter
                .getCommandType(CommandInterpreterTestConstants.IMAGE_INFO_ONLY_VALID_COMMAND);
        Assert.assertEquals(result, CommandType.IMAGE);
    }

    @Test
    public void testGetCommandTypeWithBothParametersValid() {
        CommandType result = commandInterpreter
                .getCommandType(CommandInterpreterTestConstants.BOTH_PARAMETERS_EXIST_BARCODE_COMMAND);
        Assert.assertEquals(result, CommandType.BARCODE);
    }

    @Test
    public void testGetCommandTypeWithBothParametersValidSecond() {
        CommandType result = commandInterpreter
                .getCommandType(CommandInterpreterTestConstants.BOTH_PARAMETERS_EXIST_SECOND_BARCODE_COMMAND);
        Assert.assertEquals(result, CommandType.BARCODE);
    }

    @Test
    public void testGetCommandTypeWithBarcodeParameterOnly() {
        CommandType result = commandInterpreter
                .getCommandType(CommandInterpreterTestConstants.BOTH_PARAMETERS_EXIST_BARCODE_PARAMETER_ONLY_BARCODE_COMMAND);
        Assert.assertEquals(result, CommandType.BARCODE);
    }

    @Test
    public void testGetCommandTypeWithBarcodeParameterOnlySecond() {
        CommandType result = commandInterpreter
                .getCommandType(CommandInterpreterTestConstants.BOTH_PARAMETERS_EXIST_BARCODE_PARAMETER_ONLY_SECOND__BARCODE_COMMAND);
        Assert.assertEquals(result, CommandType.BARCODE);
    }

    @Test
    public void testGetCommandTypeWithIMGParameterOnly() {
        CommandType result = commandInterpreter
                .getCommandType(CommandInterpreterTestConstants.BOTH_PARAMETERS_EXIST_IMG_PARAMETER_ONLY_BARCODE_COMMAND);
        Assert.assertEquals(result, CommandType.IMAGE);
    }

    @Test
    public void testGetCommandTypeWithIMGParameterOnlySecond() {
        CommandType result = commandInterpreter
                .getCommandType(CommandInterpreterTestConstants.BOTH_PARAMETERS_EXIST_IMG_PARAMETER_ONLY_SECOND_BARCODE_COMMAND);
        Assert.assertEquals(result, CommandType.IMAGE);
    }

    @Test
    public void testGetParametersBothImgAndCode() {
        String result = commandInterpreter
                .getParameters(
                        CommandInterpreterTestConstants.BOTH_PARAMETERS_EXIST_BARCODE_COMMAND,
                        CommandType.BARCODE
                );
        Assert.assertEquals(CommandInterpreterTestConstants.BARCODE, result);
    }

    @Test
    public void testGetParametersBothImgAndCodeSecond() {
        String result = commandInterpreter
                .getParameters(
                        CommandInterpreterTestConstants.BOTH_PARAMETERS_EXIST_SECOND_BARCODE_COMMAND,
                        CommandType.BARCODE
                );
        Assert.assertEquals(CommandInterpreterTestConstants.BARCODE, result);
    }

    @Test
    public void testGetParametersBothParametersExistOnlyIMGIsValid() {
        String result = commandInterpreter
                .getParameters(
                        CommandInterpreterTestConstants.BOTH_PARAMETERS_EXIST_IMG_PARAMETER_ONLY_BARCODE_COMMAND,
                        CommandType.IMAGE
                );
        Assert.assertEquals(CommandInterpreterTestConstants.IMGPath, result);
    }

    @Test
    public void testGetParametersBothParametersExistOnlyIMGIsValidSecond() {
        String result = commandInterpreter
                .getParameters(
                        CommandInterpreterTestConstants.BOTH_PARAMETERS_EXIST_IMG_PARAMETER_ONLY_SECOND_BARCODE_COMMAND,
                        CommandType.IMAGE
                );
        Assert.assertEquals(CommandInterpreterTestConstants.IMGPath, result);
    }

    @Test
    public void testGetParametersBothParametersExistOnlyBarcodeIsValid() {
        String result = commandInterpreter
                .getParameters(
                        CommandInterpreterTestConstants.BOTH_PARAMETERS_EXIST_BARCODE_PARAMETER_ONLY_BARCODE_COMMAND,
                        CommandType.BARCODE
                );
        Assert.assertEquals(CommandInterpreterTestConstants.BARCODE, result);
    }

    @Test
    public void testGetParametersBothParametersExistOnlyBarcodeIsValidSecond() {
        String result = commandInterpreter
                .getParameters(
                        CommandInterpreterTestConstants.BOTH_PARAMETERS_EXIST_BARCODE_PARAMETER_ONLY_SECOND__BARCODE_COMMAND,
                        CommandType.BARCODE
                );
        Assert.assertEquals(CommandInterpreterTestConstants.BARCODE, result);
    }

    @Test
    public void testGetParametersOnlyBarcodeExists() {
        String result = commandInterpreter
                .getParameters(
                        CommandInterpreterTestConstants.BARCODE_INFO_ONLY_VALID_COMMAND,
                        CommandType.BARCODE
                );
        Assert.assertEquals(CommandInterpreterTestConstants.BARCODE, result);
    }

    @Test
    public void testGetParametersOnlyIMGExists() {
        String result = commandInterpreter
                .getParameters(
                        CommandInterpreterTestConstants.IMAGE_INFO_ONLY_VALID_COMMAND,
                        CommandType.IMAGE
                );
        Assert.assertEquals(CommandInterpreterTestConstants.IMGPath, result);
    }

    @Test
    public void testGetParametersGeneralCommand() {
        String result = commandInterpreter
                .getParameters(
                        CommandInterpreterTestConstants.GENERAL_INFO_VALID_COMMAND,
                        CommandType.GENERAL
                );
        Assert.assertEquals(CommandInterpreterTestConstants.GENERAL_INFO_PARAMETER, result);
    }

    @Test
    public void testGetParametersDescriptionCommand() {
        String result = commandInterpreter
                .getParameters(
                        CommandInterpreterTestConstants.DESCRIPTION_INFO_VALID_COMMAND,
                        CommandType.DESCRIPTION
                );
        Assert.assertEquals(CommandInterpreterTestConstants.DESCRIPTION_INFO_PARAMETER, result);
    }


}
