package command;

import com.google.gson.Gson;
import constants.CommandProcessorConstants;
import dto.BadRequest;
import enums.CommandType;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CommandProcessor {
    private HttpClient client;
    private CommandInterpreter commandInterpreter;

    public CommandProcessor() {
        client = HttpClient.newHttpClient();
        commandInterpreter = new CommandInterpreter();
    }

    /**
     * Where all the magic happens...
     * Reads a command and tries to process it to eventually print it on the console
     * If something goes wrong, an error message is returned
     */
    public String processCommand(String input) throws URISyntaxException {
        if (input == null) {
            return CommandProcessorConstants.WRONG_INPUT_MESSAGE;
        }

        CommandType commandType = commandInterpreter.getCommandType(input);
        String parameters = commandInterpreter.getParameters(input, commandType);

        String isInputValidString = isInputWrong(commandType);
        if (isInputValidString != null) {
            return isInputValidString;
        }

        String existsInLocalStorage = commandInterpreter.executeCommand(commandType, parameters);
        if (existsInLocalStorage == null) {
            String result = requestInfo(client, commandType, parameters);
            String finalResult = Objects.requireNonNullElse(result, CommandProcessorConstants.WRONG_URI_MESSAGE);
            return finalResult.trim();
        } else if (existsInLocalStorage.equals(CommandProcessorConstants.NO_SUCH_ITEM_WITH_BARCODE_MESSAGE)) {
            return CommandProcessorConstants.NO_SUCH_ITEM_WITH_BARCODE_MESSAGE;
        } else {
            return existsInLocalStorage.trim();
        }
    }

    /**
     * Makes a request async
     */
    private CompletableFuture<String> getFoodAsync(HttpClient client, HttpRequest request) {
        return client.sendAsync(request, BodyHandlers.ofString()).thenApply(HttpResponse::body);
    }

    /**
     * Makes a request to the FoodDataCentral API to get the desired food information
     * then proceeds to executeCommand providing the acquired data
     * Returns an error message if a bad request occurs
     */
    private String requestInfo(HttpClient client, CommandType commandType, String parameters) throws URISyntaxException {
        HttpRequest request = createHttpRequest(commandType, parameters);
        if (request == null) {
            return null;
        }

        CompletableFuture<String> foodInfo = getFoodAsync(client, request);
        foodInfo.join();

        String json = null;
        try {
            json = foodInfo.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        BadRequest badRequest = gson.fromJson(json, BadRequest.class);
        if (badRequest.getError() != null) {
            return CommandProcessorConstants.BAD_REQUEST_MESSAGE;
        }

        return commandInterpreter.executeCommandForNonExisting(commandType, json, parameters);
    }

    /**
     * Creates an HttpRequest if the totalPath is valid
     * otherwise returns null
     */
    private HttpRequest createHttpRequest(CommandType commandType, String parameters) throws URISyntaxException {
        String foodSearch = String.format(commandType.getValue(), parameters);

        String totalPath = CommandProcessorConstants.SCHEME_HOST_PATH +
                foodSearch +
                CommandProcessorConstants.API_KEY;

        if (!isValidUri(totalPath)) {
            return null;
        }

        URI uri = new URI(totalPath);
        return HttpRequest.newBuilder(uri).build();
    }

    /**
     * Checks if an URI can be made using a given URL in the form of a string
     */

    private boolean isValidUri(String totalPath) {
        URI uri = null;
        try {
            uri = new URI(totalPath);
        } catch (URISyntaxException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the commandType is not null
     * If the commandType is null, returns error message,
     * otherwise returns null
     */
    private String isInputWrong(CommandType commandType) {
        if (commandType == null) {
            return CommandProcessorConstants.WRONG_COMMAND_MESSAGE;
        }

        return null;
    }

}