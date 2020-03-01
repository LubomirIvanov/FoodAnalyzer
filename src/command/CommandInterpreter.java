package command;

import com.google.gson.Gson;
import constants.CommandInterpreterConstants;
import database.PseudoDatabase;
import decoder.ImageDecoder;
import dto.Food;
import dto.FoodDetails;
import dto.FoodInfo;
import enums.CommandType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandInterpreter {

    private List<String> barcodePatterns;
    private List<String> IMGPatterns;

    private PseudoDatabase pseudoDatabase = PseudoDatabase.getInstance();
    private ImageDecoder decoder;

    public CommandInterpreter() {
        decoder = new ImageDecoder();

        barcodePatterns = new ArrayList<>(Arrays.asList(
                CommandInterpreterConstants.FIRST_BARCODE_PATTERN,
                CommandInterpreterConstants.SECOND_BARCODE_PATTERN,
                CommandInterpreterConstants.THIRD_BARCODE_PATTERN,
                CommandInterpreterConstants.FOURTH_BARCODE_PATTERN
        ));
        IMGPatterns = new ArrayList<>(Arrays.asList(
                CommandInterpreterConstants.FIRST_IMG_PATTERN,
                CommandInterpreterConstants.SECOND_IMG_PATTERN
        ));
    }

    /**
     * The method reads a commandLine and tries to match it to a commandType.
     * If it is not possible, returns null;
     */
    public CommandType getCommandType(String commandLine) {
        String[] tokens = commandLine.split(" ");
        if (tokens.length < 2) {
            return null;
        }

        String command = tokens[0];
        if (command.equals(CommandInterpreterConstants.GENERAL_COMMAND)) {
            return CommandType.GENERAL;
        } else if (command.equals(CommandInterpreterConstants.DESCRIPTION_COMMAND)) {
            return CommandType.DESCRIPTION;
        }

        String commandParameter = Arrays.stream(tokens).skip(1).collect(Collectors.joining(" "));

        if (tokens.length == 2) {

            if (commandParameter.matches(CommandInterpreterConstants.BARCODE_COMMAND)) {
                return CommandType.BARCODE;
            } else if (commandParameter.matches(CommandInterpreterConstants.IMAGE_COMMAND)) {
                return CommandType.IMAGE;
            }
        } else {
            for (String pattern : barcodePatterns) {
                if (commandParameter.matches(pattern)) {
                    return CommandType.BARCODE;
                }
            }

            for (String pattern : IMGPatterns) {
                if (commandParameter.matches(pattern)) {
                    return CommandType.IMAGE;
                }
            }
        }

        return null;
    }

    /**
     * The method reads a commandLine and a commandType and tries to extract the command parameters.
     * If the commandType parameter is null, returns null;
     */
    public String getParameters(String commandLine, CommandType type) {
        String[] tokens = commandLine.split(" ");

        if (type == CommandType.GENERAL) {
            return Arrays
                    .stream(tokens)
                    .skip(1)
                    .collect(Collectors.joining(CommandInterpreterConstants.SPACE_DELIMITER));
        } else if (type == CommandType.DESCRIPTION) {
            return Arrays
                    .stream(tokens)
                    .skip(1)
                    .collect(Collectors.joining(CommandInterpreterConstants.SPACE_DELIMITER));
        }

        if (type == CommandType.BARCODE || type == CommandType.IMAGE) {
            if (tokens.length == 2) {
                return tokens[1].split("=")[1];
            } else if (tokens.length == 3) {
                if (containsParameter(tokens, 1) && containsParameter(tokens, 2)) {
                    if (tokens[1].split("=")[0].equals(CommandInterpreterConstants.CODE_COMMAND)) {
                        return tokens[1].split("=")[1];
                    } else {
                        return tokens[2].split("=")[1];
                    }
                } else if (containsParameter(tokens, 1) && !containsParameter(tokens, 2)) {
                    return tokens[1].split("=")[1];
                } else if (!containsParameter(tokens, 1) && containsParameter(tokens, 2)) {
                    return tokens[2].split("=")[1];
                }
            }
        }
        return null;
    }

    private boolean containsParameter(String[] tokens, int position) {
        return tokens[position].split("=").length == 2;
    }

    /**
     * Returns the generalInfo about a product in the form of a String
     */
    private String printGeneralInfo(String json, String parameters) {
        Gson gson = new Gson();
        FoodInfo result = gson.fromJson(json, FoodInfo.class);
        if (result.getFoods().length == 0) {
            return CommandInterpreterConstants.NO_PRODUCTS_FOUND_MESSAGE;
        }

        if (!checkIfExistsInGeneralMap(parameters)) {
            pseudoDatabase.getGeneralFoodInfo().put(parameters, result);
        }
        StringBuilder sb = new StringBuilder();

        for (var x : result.getFoods()) {
            sb.append(CommandInterpreterConstants.NEXT_ELEMENT_MESSAGE);
            sb.append(System.lineSeparator());
            sb.append(x.toString());
            sb.append(System.lineSeparator());

            if (x.getGtinUpc() != null) {
                if (!checkIfExistsInBarcodeMap(x.getGtinUpc())) {
                    pseudoDatabase.getBarcodeToQueryMap().put(x.getGtinUpc(), x);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Returns the generalInfo about a product if it is already in the pseudoDatabase in the form of a String
     */
    private String printGeneralInfoLocal(FoodInfo foodInfo) {
        StringBuilder sb = new StringBuilder();
        for (var x : foodInfo.getFoods()) {
            sb.append(CommandInterpreterConstants.NEXT_ELEMENT_MESSAGE);
            sb.append(System.lineSeparator());
            sb.append(x.toString());
            sb.append(System.lineSeparator());
            sb.append(CommandInterpreterConstants.LOCAL_STORAGE_MESSAGE);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * Returns the description info about a product in the form of a String
     */
    private String printDescriptionInfo(String json, String parameters) {

        Gson gson = new Gson();
        FoodDetails result = gson.fromJson(json, FoodDetails.class);
        if (!checkIfExistsInDescriptionMap(parameters)) {
            pseudoDatabase.getDescriptionFoodInfo().put(parameters, result);
        }

        return result.toString();
    }

    /**
     * Returns the description info about a product if it is already in the pseudoDatabase in the form of a String
     */
    private String printDescriptionLocal(FoodDetails details) {
        return details.toString() +
                System.lineSeparator() +
                CommandInterpreterConstants.LOCAL_STORAGE_MESSAGE +
                System.lineSeparator();
    }

    public String executeCommand(CommandType commandType, String parameters) {
        if (commandType == CommandType.GENERAL) {
            if (checkIfExistsInGeneralMap(parameters)) {
                return printGeneralInfoLocal(pseudoDatabase.getGeneralFoodInfo().get(parameters));
            }
        } else if (commandType == CommandType.DESCRIPTION) {
            if (checkIfExistsInDescriptionMap(parameters)) {
                return printDescriptionLocal(pseudoDatabase.getDescriptionFoodInfo().get(parameters));
            }
        } else if (commandType == CommandType.BARCODE) {
            if (checkIfExistsInBarcodeMap(parameters)) {
                return printBarcodeLocal(pseudoDatabase.getBarcodeToQueryMap().get(parameters));
            }
            return CommandInterpreterConstants.NO_SUCH_ITEM_WITH_BARCODE_MESSAGE;

        } else if (commandType == CommandType.IMAGE) {
            String gtinUpc = decoder.decodeImage(parameters);
            if (gtinUpc.equals(ImageDecoder.ERROR_MESSAGE)) {
                return ImageDecoder.ERROR_MESSAGE;
            } else if (checkIfExistsInBarcodeMap(gtinUpc)) {
                return printBarcodeLocal(pseudoDatabase.getBarcodeToQueryMap().get(gtinUpc));

            }
            return CommandInterpreterConstants.NO_SUCH_ITEM_WITH_BARCODE_MESSAGE;
        }

        return null;
    }

    private String printBarcodeLocal(Food food) {
        return CommandInterpreterConstants.NEXT_ELEMENT_MESSAGE +
                System.lineSeparator() +
                food.toString();
    }

    /**
     * Executes a command if the desired data is not already in the pseudoDatabase
     */
    public String executeCommandForNonExisting(CommandType commandType, String json, String parameters) {

        if (commandType == CommandType.GENERAL) {
            return printGeneralInfo(json, parameters);
        } else if (commandType == CommandType.DESCRIPTION) {
            return printDescriptionInfo(json, parameters);
        }
        return null;
    }

    private boolean checkIfExistsInDescriptionMap(String parameters) {
        return pseudoDatabase.getDescriptionFoodInfo().containsKey(parameters);
    }

    private boolean checkIfExistsInGeneralMap(String parameters) {
        return pseudoDatabase.getGeneralFoodInfo().containsKey(parameters);
    }

    private boolean checkIfExistsInBarcodeMap(String parameters) {
        return pseudoDatabase.getBarcodeToQueryMap().containsKey(parameters);
    }


}
