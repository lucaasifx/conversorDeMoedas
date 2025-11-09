package org.example;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.InputMismatchException;
import java.util.Scanner;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {

    private static final String API_KEY = loadApiKey();

    private static String loadApiKey() {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
            String key = prop.getProperty("API_KEY");
            if (key == null || key.isEmpty()) {
                throw new RuntimeException("API_KEY não encontrada em config.properties");
            }
            return key;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar config.properties. " +
                    "Certifique-se que o arquivo existe na raiz do projeto e contém a API_KEY.", e);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option = 0;

        while (true) {
            System.out.println("Seja bem-vindo/a ao Conversor de Moeda =]");
            System.out.println();
            System.out.println("1) Dólar => Peso argentino");
            System.out.println("2) Peso argentino => Dólar");
            System.out.println("3) Dólar => Real brasileiro");
            System.out.println("4) Real brasileiro => Dólar");
            System.out.println("5) Dólar => Peso colombiano");
            System.out.println("6) Peso colombiano => Dólar");
            System.out.println("7) Sair");
            System.out.println();

            System.out.println("Escolha uma opção válida:");
            System.out.println("**************************************************");

            try {
                option = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número de 1 a 7.");
                System.out.println();
                scanner.next();
                continue;
            }

            if (option == 7) {
                System.out.println("Saindo... Obrigado por usar o conversor!");
                scanner.close();
                return;
            }

            switch (option) {
                case 1:
                    performConversion(scanner, "USD", "ARS");
                    break;
                case 2:
                    performConversion(scanner, "ARS", "USD");
                    break;
                case 3:
                    performConversion(scanner, "USD", "BRL");
                    break;
                case 4:
                    performConversion(scanner, "BRL", "USD");
                    break;
                case 5:
                    performConversion(scanner, "USD", "COP");
                    break;
                case 6:
                    performConversion(scanner, "COP", "USD");
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, escolha um número entre 1 e 7.");
            }

            System.out.println();
        }
    }

    private static JsonObject getPairRateData(String baseCurrency, String targetCurrency) {
        try {
            String url_str = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/" + baseCurrency + "/" + targetCurrency;

            URL url = new URL(url_str);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            if (request.getResponseCode() != 200) {
                System.out.println("Erro ao conectar na API: " + request.getResponseMessage());
                return null;
            }

            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();

            String result = jsonobj.get("result").getAsString();
            if (!result.equals("success")) {
                System.out.println("Erro retornado pela API: " + jsonobj.get("error-type").getAsString());
                return null;
            }

            return jsonobj;

        } catch (Exception e) {
            System.out.println("Ocorreu um erro na requisição HTTP: " + e.getMessage());
            return null;
        }
    }

    private static void performConversion(Scanner scanner, String baseCurrency, String targetCurrency) {
        System.out.println("Digite o valor em " + baseCurrency + " a ser convertido:");
        double amount;

        try {
            amount = scanner.nextDouble();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Valor inválido. A operação foi cancelada.");
            scanner.nextLine();
            return;
        }

        System.out.println("Buscando cotação...");
        JsonObject ratesData = getPairRateData(baseCurrency, targetCurrency);

        if (ratesData != null) {
            try {
                double rate = ratesData.get("conversion_rate").getAsDouble();
                double convertedAmount = amount * rate;

                System.out.printf("-> %.2f %s = %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);

            } catch (Exception e) {
                System.out.println("Erro ao processar a resposta JSON: " + e.getMessage());
            }
        } else {
            System.out.println("Não foi possível obter a cotação. Verifique sua conexão ou chave de API.");
        }
    }
}