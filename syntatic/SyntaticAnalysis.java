package syntatic;

import javax.lang.model.type.TypeKind;

import interpreter.command.Command;
import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.TokenType;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;

    public SyntaticAnalysis(LexicalAnalysis lex) {
        this.lex = lex;
        this.current = lex.nextToken();
    }

    public Command start() {
        return null;
    }

    private void advance() {
        // System.out.println("Advanced (\"" + current.token + "\", " +
        //     current.type + ")");
        current = lex.nextToken();
    }

    private void eat(TokenType type) {
        // System.out.println("Expected (..., " + type + "), found (\"" + 
        //     current.token + "\", " + current.type + ")");
        if (type == current.type) {
            advance();
        } else {
            showError();
            
        }
    }

    private void showError() {
        System.out.printf("%02d: ", lex.getLine());

        switch (current.type) {
            case INVALID_TOKEN:
                System.out.printf("Lexema inválido [%s]\n", current.token);
                break;
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                System.out.printf("Fim de arquivo inesperado\n");
                break;
            default:
                System.out.printf("Lexema não esperado [%s]\n", current.token);
                break;
        }

        System.exit(1);
    }

    public void procCmdList() {
        while(current.type == TokenType.UNLESS ||
         current.type == TokenType.WHILE || 
         current.type == TokenType.IF ||
         current.type == TokenType.UNTIL ||
         current.type == TokenType.FOR ||
         current.type == TokenType.PRINT ||
         current.type == TokenType.PUTS ||
         current.type == TokenType.ASSIGN){
            procCmd();
        }
    }

    public void procCmd() {
        if(current.type == TokenType.IF){
            procIf();
        }
        else if(current.type == TokenType.UNLESS){
            procUnless();
        }
        else if(current.type == TokenType.WHILE){
            procWhile();
        }
        else if(current.type == TokenType.UNTIL){
            procUntil();
        }
        else if(current.type == TokenType.FOR){
            procFor();
        }
        else if(current.type == TokenType.PUTS || current.type == TokenType.PRINT){ //Não existe Output
            procOutput();
        }
        else if(current.type == TokenType.ASSIGN){
            procAssign();
        }
        else{
            showError();
        }
    }

    public void procIf() {
        eat(TokenType.IF);
        procBoolExpr();
        eat(TokenType.THEN);
        procCmdList();
        while(current.type == TokenType.ELSIF){
            eat(TokenType.ELSIF);
            procBoolExpr();
            eat(TokenType.THEN);
            procCmdList();
        }
        if(current.type == TokenType.ELSE){
            advance();
            procCmdList();
        }
        eat(TokenType.END);
    }

    public void procUnless() {
        eat(TokenType.UNLESS);
        procBoolExpr();
        eat(TokenType.THEN);
        procCmdList();
        if(current.type == TokenType.ELSE){
            advance();
            procCmdList();
        }
        eat(TokenType.END);
    }

    public void procWhile() {
        eat(TokenType.WHILE);
        procBoolExpr();
        if(current.type == TokenType.DO){
            eat(TokenType.DO);
        }
        procCmdList();
        eat(TokenType.END);
    }

    public void procUntil() {
        eat(TokenType.UNTIL);
        procBoolExpr();
        if(current.type == TokenType.DO){
            eat(TokenType.DO);
        }
        procCmdList();
        eat(TokenType.END);
    }

    public void procFor() {
        eat(TokenType.FOR);
        procId();
        eat(TokenType.IN);
        procExpr();
        if(current.type == TokenType.DO){
            eat(TokenType.DO);
        }
        procCmdList();
        eat(TokenType.END);
    }

    public void procOutput() {                          //output com problemas
        if(current.type == TokenType.PUTS){
            eat(TokenType.PUTS);
            procExpr();
        }
        else  if(current.type == TokenType.PRINT){
            eat(TokenType.PRINT);
        }
        eat(TokenType.SEMI_COLON);
    }

    public void procAssign() {
        //TO_do
    }

    public void procPost() {
        if(current.type == TokenType.IF){
            eat(TokenType.IF);
            procBoolExpr();
        }
        else if(current.type == TokenType.UNLESS){
            eat(TokenType.UNLESS);
            procBoolExpr();
        }
    }

    public void procBoolExpr() {
        if(current.type==TokenType.NOT){
            eat(TokenType.NOT);
        }
        procCmpExpr();
        if(current.type ==  TokenType.AND){
            eat(TokenType.AND);
            procBoolExpr();
        }
        else if(current.type == TokenType.OR){
            eat(TokenType.OR);
            procBoolExpr();
        }
    }

    public void procCmpExpr() {
        procExpr();
        if(current.type == TokenType.EQUALS ||
            current.type == TokenType.NOT_EQUALS || 
            current.type == TokenType.LOWER ||  
            current.type == TokenType.GREATER || 
            current.type == TokenType.LOWER_EQ || 
            current.type == TokenType.GREATER_EQ || 
            current.type == TokenType.CONTAINS){
                advance();
        }
        procExpr();
    }

    public void procExpr() {
        procArith();
        if(current.type == TokenType.RANGE_WITH|| current.type == TokenType.RANGE_WITHOUT){
            procArith();
        }
    }

    public void procArith() {
        procTerm();
        while(current.type == TokenType.ADD || current.type==TokenType.SUB)
            procTerm();
    }

    public void procTerm() {
        procPower();
        while(current.type == TokenType.MUL|| current.type == TokenType.DIV || current.type == TokenType.MOD)
            procPower();
    }

    public void procPower() {
        procFactor();
        while(current.type == TokenType.EXP)
            procFactor();
    }

    public void procFactor() {
        if(current.type == TokenType.ADD || current.type == TokenType.SUB){
            advance();
        }
        //TO_DO
    }


}

