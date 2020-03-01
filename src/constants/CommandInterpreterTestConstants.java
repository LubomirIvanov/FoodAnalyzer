package constants;

public class CommandInterpreterTestConstants {
    public static final String MISSING_PARAMETER_GENERAL_INFO_COMMAND = "get-food";
    public static final String INVALID_COMMAND = "get-foo raffaello treat";
    public static final String MISSING_PARAMETER_BARCODE_INFO_COMMAND = "get-food-by-barcode --code=";
    public static final String MISSING_PARAMETER_IMG_INFO_COMMAND = "get-food-by-barcode --code=";
    public static final String MISSING_BOTH_PARAMETERS_COMMAND = "get-food-by-barcode --code= --img=";

    public static final String GENERAL_INFO_VALID_COMMAND = "get-food raffaello treat";
    public static final String DESCRIPTION_INFO_VALID_COMMAND = "get-food-report 415269";
    public static final String BARCODE_INFO_ONLY_VALID_COMMAND = "get-food-by-barcode --code=009800146130";
    public static final String IMAGE_INFO_ONLY_VALID_COMMAND = "get-food-by-barcode --img=D:\\Photos\\BarcodeImage.jpg";

    public static final String BOTH_PARAMETERS_EXIST_BARCODE_COMMAND =
            "get-food-by-barcode --img=D:\\Photos\\BarcodeImage.jpg --code=009800146130";
    public static final String BOTH_PARAMETERS_EXIST_SECOND_BARCODE_COMMAND =
            "get-food-by-barcode --code=009800146130 --img=D:\\Photos\\BarcodeImage.jpg";
    public static final String BOTH_PARAMETERS_EXIST_BARCODE_PARAMETER_ONLY_BARCODE_COMMAND =
            "get-food-by-barcode --img= --code=009800146130";
    public static final String BOTH_PARAMETERS_EXIST_BARCODE_PARAMETER_ONLY_SECOND__BARCODE_COMMAND =
            "get-food-by-barcode --code=009800146130 --img= ";
    public static final String BOTH_PARAMETERS_EXIST_IMG_PARAMETER_ONLY_BARCODE_COMMAND =
            "get-food-by-barcode --img=D:\\Photos\\BarcodeImage.jpg --code= ";
    public static final String BOTH_PARAMETERS_EXIST_IMG_PARAMETER_ONLY_SECOND_BARCODE_COMMAND =
            "get-food-by-barcode --code= --img=D:\\Photos\\BarcodeImage.jpg";

    public static final String BARCODE ="009800146130";
    public static final String IMGPath ="D:\\Photos\\BarcodeImage.jpg";
    public static final String GENERAL_INFO_PARAMETER ="raffaello%20treat";
    public static final String DESCRIPTION_INFO_PARAMETER="415269";
}
