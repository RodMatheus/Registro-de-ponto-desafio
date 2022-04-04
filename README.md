# DESAFIO: Registro de ponto eletrônico

Api para registro de ponto eletrônico feita para resolver o desafio proposto pela [**Ília Digital**](www.ilia.digital).

## Requisitos

### Java 17
O Java pode ser instalado por meio da JDK no site da [Oracle](https://www.oracle.com/java/technologies/downloads/) ou no site da [OpenJDK](https://jdk.java.net/java-se-ri/17). Como alternativa a essa instalação manual e gerenciamento, você pode utilizar o [SDKMan](https://sdkman.io/) e instalar/gerenciar suas versões de java através dele.

### Maven **(Opcional)**
A instalação do Maven é opcional, é possível atrás do mvnw.[cmd/sh] para Windows e Linux respectivamente, contidos na raíz do projeto, executar comandos de configuração e build do projeto. Mas caso queira executar o projeto com uma instalação local do Maven, você também pode utilizar o [SDKMan](https://sdkman.io/) para isso.

### Docker e Docker-compose
O [Docker](https://www.docker.com/get-started/) pode ser instalado em praticamente todos os sistemas operacionais, para Windows por exemplo a instalação será do Docker Desktop, onde o mesmo já vem com o Docker-compose. Nos demais sistemas, caso não hajam a instalação do Docker-compose, sugiro que a faça.

### Estrutura
![image](https://user-images.githubusercontent.com/28794903/161561316-3da43d03-548c-4fc6-93af-5e5810e367f9.png)

### Arquitetura
Abaixo a lista de tecnologias usadas em todo o ecossistema deste projeto:
- [Java 17](https://www.java.com/pt-BR/)
- [Lombok](https://projectlombok.org/)
- [Mapstruct](https://mapstruct.org/)
- [Maven](https://maven.apache.org/)
- [TestContainer](https://www.testcontainers.org/)
- [Spring Boot 2.6.6](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Security oAuth](https://spring.io/projects/spring-security-oauth)
- [Keycloak 17](https://www.keycloak.org/)
- [PostgresSQL](https://www.postgresql.org/)

### Execução local do projeto
Antes de mais nada, o projeto lhe dá todo o suporte para que você possa executá-lo localmente, para isso, vá até a pasta na raiz do projeto chamada **infra-local** dentro dela, existe um docker-compose já configurado com toda a infraestrutura a parte necessária.

Execute o comando **docker-compose up na raíz da pasta infra-local**. Ao fazer isso um container com PostgreSQL e outro com um Keycloak serão criados para que você possa se conectar com o projeto.

### Configuração de ambiente local

#### PostgreSQL
Uma vez que o container está em atividade, você pode se conectar ao servidor de banco de dados através de sua IDE de banco de dados favorita localmente e com a porta padrão (5432), às informações de usuário e senha do banco estão nas variáveis de ambiente do arquivo ./postrgres/docker-compose. Acesse o servidor, crie um usuário e a sua base de dados preferencialmente chamada de "ponto_eletronico" associada a este usuário.

#### Keycloak
Uma vez que o container está em atividade, você pode se conectar ao seu Keycloak atrás url http://localhost:8081/admin e para entrar use o usuário e senha de admin contidos nas variáveis de ambiente do arquivo ./keycloak/docker-compose. Ao entrar no sistema, no canto superior esquerdo em **Master**, clique em "add realm" e no campo "import" utilize o arquivo ./keycloak/realm-ilia-ponto.json e depois em "create".

#### Profiles
Ao realizar toda a configuração do PostgreSQL e Keycloak, você precisará configurar o seu Spring Profile para executar o seu projeto, para isso, basta preencher corretamente as variáveis de ambiente do projeto, o arquivo application-example.properties será o seu modelo para preenchimento ou para criar o seu próprio arquivo de ambiente.

Preencha os campos de configuração tanto de securança, como de banco de dados. Por exemplo:

spring.datasource.password=[SENHA_DEFINIDA NO_BANCO]
spring.datasource.username=[USUARIO_DEFINIDO NO_BANCO]
spring.datasource.url=jdbc:postgresql://localhost:5432/[BASE_DEFINIDA_NO_BANCO]

spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8081/realms/ilia_ponto
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8081/realms/ilia_ponto/protocol/openid-connect/certs

### Execução em ambiente local
Feita toda a configuração de ambiente local, basta que você importe o projeto na sua IDE preferida e o execute.

#### Postman
Caso queria chamar os endpoints da aplicação, você precisará se autenticar, no Postman, selecione a opção de authorization "oAuth2" e utilize a seguinte configuração:

![image](https://user-images.githubusercontent.com/28794903/161572896-af22b9d5-bd60-47c8-99d7-4c36632b8adc.png)

Vá até o Keycloak, clique em: Clients -> ilia_ponto -> Credentials -> Generete secret, ao fazer isso, cole o secret no campo "Client Secret" do Postman.
 Os campos "Auth URL" e "Access Token URL" devem ter os respectivos valores: http://localhost:8081/realms/ilia_ponto/protocol/openid-connect/auth e http://localhost:8081/realms/ilia_ponto/protocol/openid-connect/token.
 
Assim ao clicar em "Get new Access Token" você redirecionado para uma página do Keycloak para logar-se, como seria feito em uma aplicação de front-end qualquer. Use o usuário teste@teste.com e senha asdf1234, que foram criados no momento da importação do seu realm. Assim você obterá seu token de acesso e poderá executar às suas chamadas.

### Build em ambiente local
Caso queira compilar seu projeto localmente, configure em sua IDE um build Maven e em Goals preencha com: clean package  -Dspring.profiles.active=[arquivo_properties] caso queria executar o build e todos os testes (unitários e de integração).

Caso queira executar todos os testes configure um build Maven e em Goals preencha com: test
Caso queira pular todos os testes configure o build Maven e em Goals preencha com: clean package -Dspring.profiles.active=[arquivo_properties] -DskipTests e para pular apenas os testes integrados: clean package -Dspring.profiles.active=[arquivo_properties] -DskipIntegrationTests.

Obs.: Sempre que for executar um build sem pular os testes integrados, esteja com o Docker inicializado em sua máquina.
