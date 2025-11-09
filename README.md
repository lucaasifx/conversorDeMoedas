# Conversor de Moedas

## Descrição

Este é um simples conversor de moedas que funciona no console. Ele permite ao usuário converter valores entre diferentes moedas, utilizando as taxas de câmbio mais recentes obtidas através de uma API.

## Funcionalidades

- Converte Dólar (USD) para Peso Argentino (ARS) e vice-versa.
- Converte Dólar (USD) para Real Brasileiro (BRL) e vice-versa.
- Converte Dólar (USD) para Peso Colombiano (COP) e vice-versa.
- Interface de linha de comando interativa.

## Como Executar

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/seu-usuario/conversorDeMoedas.git
    cd conversorDeMoedas
    ```

2.  **Configure a API Key:**
    Crie um arquivo `config.properties` na raiz do projeto e adicione sua chave da API da `exchangerate-api.com`:
    ```properties
    API_KEY=sua_chave_de_api
    ```

3.  **Compile e execute o projeto usando Maven:**
    ```bash
    mvn compile
    mvn exec:java -Dexec.mainClass="org.example.Main"
    ```

## Configuração

Para que a aplicação funcione, você precisa de uma chave de API da [ExchangeRate-API](https://www.exchangerate-api.com/). Crie uma conta e obtenha sua chave de API gratuita.

## Tecnologias Utilizadas

- Java 25
- Maven
- Gson
- ExchangeRate-API
