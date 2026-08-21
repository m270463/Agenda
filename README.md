# Projeto MC322 - Agenda Pessoal e Acadêmica: Organização de Estudos na UNICAMP

Este projeto foi desenvolvido como parte dos laboratórios e do projeto final da disciplina **MC322 - Programação Orientada a Objetos** na UNICAMP.

A rotina em Barão Geraldo pode ser caótica entre provas, laboratórios e projetos. O objetivo deste sistema é implementar uma **Agenda Pessoal e Acadêmica** robusta com interface gráfica, na qual o usuário gerencia seus compromissos e eventos recorrentes de forma visual, intuitiva e protegida por login.

O projeto foi desenvolvido em **Java 17+** utilizando **JavaFX 21** para a interface gráfica, **SQLite** para persistência local dos dados e **Gradle** para automação de build, execução e geração de relatórios de cobertura (JaCoCo), aplicando fortes conceitos de Programação Orientada a Objetos (Herança, Polimorfismo, Interfaces e Arquitetura MVC).

## Demo

> 🎥 [Insira aqui o link da demonstração do aplicativo]

- [1. Estrutura do Projeto](#1-estrutura-do-projeto)
- [2. Como Compilar e Executar o Projeto](#2-como-compilar-e-executar-o-projeto)
- [3. Como Usar o Sistema](#3-como-usar-o-sistema)
- [4. Tecnologias Utilizadas](#4-tecnologias-utilizadas)
- [5. Autores](#5-autores)

## 1. Estrutura do Projeto

O projeto segue a estrutura padrão do **Gradle para projetos Java e JavaFX**, dividindo as responsabilidades em pacotes lógicos:

. ├─ app/ │ ├─ src/ │ │ ├─ main/ │ │ │ ├─ java/com/agenda/ │ │ │ │ ├─ App.java # Classe principal (Inicializadora da GUI e System Tray) │ │ │ │ ├─ MainLauncher.java # Ponto de entrada alternativo da aplicação │ │ │ │ ├─ LoginController.java # Controle de login e autenticação │ │ │ │ ├─ CriacontaController.java # Controle do cadastro de novos usuários │ │ │ │ ├─ editarUsuarioController.java # Edição de dados da conta com confirmação por e-mail │ │ │ │ ├─ calendarioController.java # Visualização mensal do calendário │ │ │ │ ├─ verEventoController.java # Exibição e gestão diária de compromissos │ │ │ │ ├─ controllerEventos.java # Classe abstrata base (Criar/Editar evento) │ │ │ │ ├─ CriaeventoController.java # Criação de compromissos │ │ │ │ ├─ EditEventoController.java # Edição e remoção de compromissos │ │ │ │ ├─ Usuario.java # Entidade de usuário e credenciais │ │ │ │ ├─ ItemAgenda.java # Classe abstrata base de itens da agenda │ │ │ │ ├─ Evento.java # Entidade do compromisso/evento │ │ │ │ ├─ Repetivel.java # Interface de regras de recorrência de data │ │ │ │ ├─ Validavel.java # Interface para validação de formulários │ │ │ │ ├─ Persistivel.java # Interface para padronização de persistência │ │ │ │ ├─ GerenciadorDados.java # Persistência via SQLite (JDBC) │ │ │ │ ├─ Notificador.java # Agendamento e disparo de lembretes diários │ │ │ │ ├─ ServicoEmail.java # Envio de e-mails via SMTP (Jakarta Mail) │ │ │ │ ├─ ValidadorDominio.java # Validação de domínio de e-mail (registros MX) │ │ │ │ └─ Config.java # Leitura de credenciais via variáveis de ambiente │ │ │ └─ resources/com/agenda/ # Telas visuais .fxml (teste, criarconta, editarusuario, │ │ │ # calendario, criaevento, editevento, verevento) │ │ └─ test/ │ │ └─ java/com/agenda/ │ │ ├─ TesteFluxoEventos.java # Testes de fluxo: calendário → criação de evento │ │ └─ TesteInterface.java # Testes de fluxo: login e criação de conta ├─ build.gradle # Configurações do build, dependências e plugins ├─ settings.gradle # Módulos ativos do projeto Gradle ├─ agenda.db # Banco de dados SQLite local ├─ .env # Variáveis de ambiente (credenciais de e-mail) ├─ gradlew # Wrapper do Gradle para Linux/macOS ├─ gradlew.bat # Wrapper do Gradle para Windows └─ README.md

Onde:

- `app/src/main/java` — Código-fonte contendo as regras de negócio, persistência, envio de e-mails e controladores da interface gráfica.
- `app/src/main/resources` — Arquivos FXML estruturando visualmente as telas da aplicação.
- `app/src/test/java` — Classes de testes automatizados de ponta a ponta com JUnit 5 e TestFX, simulando cliques e interações reais do usuário na interface.
- `agenda.db` — Banco de dados SQLite local onde contas, eventos e verificações de notificação são armazenados.
- `.env` — Arquivo de variáveis de ambiente contendo as credenciais de e-mail usadas para autenticação e envio de notificações/códigos de confirmação.
- `build.gradle` — Configurações do compilador, dependências de JavaFX, SQLite JDBC, Jakarta Mail, dotenv, ControlsFX, JUnit 5, TestFX e motor do JaCoCo.

## 2. Como Compilar e Executar o Projeto

No diretório raiz do projeto, execute o comando abaixo no terminal para rodar os testes (recomenda-se `clean` antes, para garantir que nenhum recurso antigo de builds anteriores fique em cache):

./gradlew clean test

Para rodar os testes e gerar o relatório de cobertura JaCoCo:

./gradlew clean test jacocoTestReport

O relatório é gerado em `/app/build/reports/jacoco/test/html/index.html`.

Para gerar a documentação das classes (Javadoc), disponibilizado em `/app/build/docs/javadoc/index.html`:

./gradlew javadoc

Para **abrir o aplicativo** de verdade (janela JavaFX, sem rodar testes):

./gradlew run

Para compilar e empacotar o projeto por completo (compila, roda os testes e gera o `.jar`):

./gradlew build

O Gradle irá:

- Baixar e gerenciar dependências de forma transparente.
- Compilar os pacotes gráficos e de testes.
- Rodar a suíte de testes de interface (login, cadastro e fluxo completo de criação de eventos).
- Gerar o site estático com o relatório de cobertura em `/app/build/reports/jacoco/test/html/index.html`.

**Observação:** para que o envio de e-mails (notificações e códigos de confirmação) funcione corretamente, é necessário configurar um arquivo `.env` na raiz do projeto com as variáveis `EMAIL` e `SENHA`, correspondentes às credenciais de uma conta de e-mail habilitada para envio via SMTP, conforme o arquivo `.env.example`.

## 3. Como Usar o Sistema

Durante a execução da sua agenda pessoal:

- O usuário deve realizar o login digitando suas credenciais de e-mail e senha cadastrados.
- Caso não possua login, basta clicar em "Criar Conta" na tela principal para cadastrar seu Nome, E-mail, Telefone e Senha.
- O sistema realiza validações rígidas: não é permitido cadastrar e-mails em branco, com domínio sem servidor de e-mail válido, já cadastrados, nem confirmar senhas divergentes.
- Ao entrar no sistema, o usuário é apresentado a uma folha de Calendário Mensal visual interativo, podendo navegar entre meses e anos.
- O usuário pode clicar em qualquer dia do mês para abrir a visualização de todos os eventos programados para aquele dia específico.
- No painel do dia, é possível:
  - **Criar um Novo Evento:** definindo Título, Descrição, Data, Horário de Início e Fim.
  - Definir se é um evento de **Dia Inteiro** (desativando a necessidade de definir horários específicos).
  - Escolher a **Recorrência**: se o evento é Único ("Nunca") ou se repete em padrões ("Diariamente", "Semanalmente", "Mensalmente", "Anualmente").
  - **Editar ou Excluir**: alterar títulos, datas, trocar os horários ou apagar o evento definitivamente.
- **Editar Conta:** o usuário pode alterar seus dados cadastrais (nome, e-mail, telefone e senha) a qualquer momento; a confirmação da alteração exige um código de verificação enviado por e-mail.
- **Notificações por E-mail:** o sistema verifica periodicamente os compromissos do dia seguinte do usuário ativo e envia um lembrete automático por e-mail listando os eventos programados.
- **Bandeja do Sistema (System Tray):** ao fechar a janela, o aplicativo continua rodando em segundo plano na bandeja do sistema, podendo ser reaberto ou encerrado definitivamente por lá.
- **Validações de Horários:** o sistema impede logicamente que o horário de término de um evento seja definido antes do horário de início, e rejeita datas calendário inexistentes (ex: 31/02).
- **Persistência Automática:** os dados dos usuários e eventos são salvos automaticamente no banco de dados SQLite local a cada operação, garantindo que nada seja perdido entre execuções.

## 4. Tecnologias Utilizadas

- **Java 17+** (Switch Expressions, java.time API)
- **JavaFX 21** (Ambiente de janelas e componentes gráficos de interface)
- **ControlsFX** (Componentes adicionais de interface, como o ToggleSwitch)
- **Scene Builder** (Prototipagem e montagem visual das telas `.fxml` da interface gráfica)
- **Gradle** (Automação de compilação, ciclo de execução e dependências)
- **SQLite (JDBC)** (Banco de dados relacional local para persistência de usuários e eventos)
- **Jakarta Mail** (Envio de e-mails de notificação e códigos de confirmação via SMTP)
- **Dotenv (java-dotenv)** (Gerenciamento seguro de variáveis de ambiente e credenciais)
- **JUnit 5 & TestFX** (Testes automatizados de ponta a ponta simulando interação real do usuário)
- **JaCoCo** (Relatório de cobertura de código integrado ao build)

Durante o desenvolvimento, também foi utilizado apoio de **Inteligência Artificial** como ferramenta de produtividade em três frentes específicas: elaboração e depuração dos testes automatizados (JUnit + TestFX), geração da documentação Javadoc das classes, e apoio na estruturação de partes da interface gráfica (telas `.fxml` e estilização).

## 5. Autores

- [Matheus Assis / 270463]
- [Theo Couto / 172427]
- [João Neto / 269172]
- [Caio Veloso/ 253448]
