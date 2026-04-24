# 🌤️ Clima API 

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0+-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

Bem-vindo(a) ao repositório da **Clima API**! 👋

Desenvolvi esse backend usando Java e Spring Boot.
Na prática, a aplicação consome as informações brutas do OpenWeatherMap e do GeoIP, faz todo o trabalho (filtrar os dados, calcular as médias de previsão, tratar erros) e devolve um JSON redondo e organizado.

---

## 🚀 O que essa API faz de legal?

* **⛅ Clima atual e previsão:** Traz o clima exato de agora e já calcula a previsão dos próximos 5 dias.
* **📍 IP (GeoIP):** Bateu na rota principal? O sistema lê o cabeçalho HTTP, descobre o IP real  e já diz a cidade do usuário automaticamente.
* **💾 Histórico seguro:** Tudo o que é pesquisado fica salvo bonitinho no banco de dados (MySQL) para futuras pesquisas.
* **🛡️ Tratamento de erros:** Se o usuário digitar uma cidade que não existe, se a API externa cair ou se o banco de dados falhar, o sistema intercepta e devolve um JSON personalizado do problema. 

---

## 👀 Exemplos de Uso e Testes (JSON)
### 📍 1. Clima Automático por IP: `GET /clima`
Se o usuário não enviar nenhuma cidade, a API lê a rede e devolve a localização atual. Rodando localmente, aponta para o Rio de Janeiro:

```json
{
  "cidade": "Rio de Janeiro",
  "agora": {
    "nome": "Rio de Janeiro",
    "temperatura": 32.5,
    "sensacao": 35.1,
    "descricao": "céu limpo"
  },
  "previsao5Dias": [
    {
      "data": "2026-04-24",
      "tempMinima": 22.8,
      "tempMaxima": 35.1,
      "descricao": "ensolarado"
    },
    {
      "data": "2026-04-25",
      "tempMinima": 24.0,
      "tempMaxima": 33.5,
      "descricao": "nuvens dispersas"
    }
  ]
}
```

### 🔍 2. Busca Específica: `GET /clima/busca/Rio de Janeiro`
Permite buscar qualquer cidade do mundo. A API trata espaços e caracteres especiais de forma segura:

```json
{
  "cidade": "Rio de Janeiro",
  "agora": {
    "nome": "Rio de Janeiro",
    "temperatura": 31.0,
    "sensacao": 32.8,
    "descricao": "algumas nuvens"
  },
  "previsao5Dias": [
    {
      "data": "2026-04-24",
      "tempMinima": 22.8,
      "tempMaxima": 35.1,
      "descricao": "ensolarado"
    }
  ]
}
```

### 💾 3. Histórico de Consultas: `GET /clima/historico`
Retorna tudo o que foi salvo no MySQL de forma segura, usando um DTO próprio que esconde IDs e dados sensíveis do banco:

```json
[
  {
    "cidade": "Rio de Janeiro",
    "temperatura": 32.5,
    "descricao": "céu limpo",
    "dataPesquisa": "2026-04-23T10:15:30.123"
  },
  {
    "cidade": "Rio de Janeiro",
    "temperatura": 31.0,
    "descricao": "algumas nuvens",
    "dataPesquisa": "2026-04-23T14:30:00.456"
  }
]
```

### ❌ 4. Tratamento Global de Erros (O Para-raios)
A classe `@RestControllerAdvice` atua como um escudo protetor, ela trata os diferentes tipos de falhas.

**Exemplo A: Cidade Não Encontrada (Erro 404)** - `GET /clima/busca/Narnia`
```json
{
  "timestamp": "2026-04-23T14:35:10.999",
  "status": 404,
  "erro": "Cidade não encontrada",
  "mensagem": "Não conseguimos localizar a cidade digitada. Verifique a ortografia e tente novamente.",
  "caminhoDaRequisicao": "/clima/busca/Narnia"
}
```

**Exemplo B: Falha no Banco ou API Externa (Erro 500)**
```json
{
  "timestamp": "2026-04-23T14:40:22.450",
  "status": 500,
  "erro": "Erro Interno",
  "mensagem": "Ops! Tivemos um problema no nosso sistema. Tente novamente mais tarde.",
  "caminhoDaRequisicao": "/clima/busca/Rio de Janeiro"
}
```

## 🔌 Testando na prática (Swagger)

A API já vem com documentação gráfica! Depois de rodar na sua máquina, é só abrir o navegador e acessar:
👉 `http://localhost:8080/swagger-ui/index.html`

Lá você pode testar todas essas rotas.

---

## ⚙️ Como rodar na sua máquina

### Passo a Passo:

1. **Clone o projeto:**
```bash
git clone git clone https://github.com/Guims0/api-clima.git
```

2. **Configure o seu Banco e a API Key:**
Abra o arquivo `src/main/resources/application.properties` e preencha a sua senha do MySQL e a sua chave gratuita do [OpenWeatherMap](https://openweathermap.org/api):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_clima?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=SUA_SENHA_AQUI
spring.jpa.hibernate.ddl-auto=update

# Cole sua chave de desenvolvedor aqui
WEATHER_API_KEY=sua_chave_secreta_aqui
```

3. **Inicie o Servidor:**
Rode pelo botão de Play na sua IDE (IntelliJ/Eclipse) ou jogue este comando no terminal:

```bash
mvn spring-boot:run
```
Pronto! A aplicação estará rodando perfeitamente na porta `8080`.

---

## 👨‍💻 Feito por
**Guilherme** ☕
