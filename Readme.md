# ToDo+ - Sistema de Gerenciamento de Tarefas

## Alunos

- João Pedro Santos de Brito
- Renan resto do nome

## Visão Geral

O **ToDo+** é um aplicativo Android para gerenciamento de tarefas pessoais com sistema de notificações. Permite criar, organizar e monitorar atividades diárias com lembretes personalizados, seguindo um modelo CRUD (Create, Read, Update, Delete).

## Usuários e Papéis

- **Usuário Padrão**:
  - Realizar cadastro/login com foto
  - Gerenciar tarefas (criar/editar/excluir)
  - Favoritar tarefas importantes
  - Filtrar tarefas por tags/status
  - Receber notificações no horário agendado

## Requisitos Funcionais

1. **Autenticação**:
   - Cadastro de usuário com foto e senha (hash)
   - Login seguro
2. **Gestão de Tarefas**:

   - Criar tarefas com: título, descrição, data/hora, tags
   - Editar/excluir tarefas existentes
   - Marcar como concluída
   - Favoritar tarefas prioritárias

3. **Organização**:

   - Filtros por: tags, favoritos, pendentes
   - Busca textual

4. **Notificações**:

   - Alarmes para horário da tarefa

5. **Recursos Adicionais**:
   

## Recursos Técnicos Implementados

- :camera: **Câmera** para foto do perfil
- :alarm_clock: **AlarmManager** para notificações
- :lock: **Armazenamento Seguro** (senhas em hash)
- :fragment: **Fragmentos** para UI modular
- :sound: **Sons** para feedback de ações
- :map: **Mapas** (opcional para tarefas com localização)

## Tratamento de Erros (Testes Caixa Preta)

| Caso de Erro               | Tratamento Implementado            |
| -------------------------- | ---------------------------------- |
| Login inválido             | Mensagem "Credenciais incorretas"  |
| Campos obrigatórios vazios | Validação em tempo real            |
| Formato de data inválido   | Orientação de correção  |
| Divisão por zero           | Prevenção em cálculos estatísticos |

## Requisitos de Qualidade

- **Usabilidade**:  
  Interface intuitiva, feedback visual imediato, tempo de resposta rápido
- **Segurança**:  
  Criptografia para dados sensíveis

---

**Repositório GitHub**: [https://github.com/brito219/ProgMov]  
**Data de Entrega**: 28/06/2025
