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
        while(true){
            //TO_Do
        }
    }

    public void procCmd(TokenType type) {
        if(type == TokenType.IF){
            procIf();
        }
        else if(type == TokenType.UNLESS){
            procUnless();
        }
        else if(type == TokenType.WHILE){
            procWhile();
        }
        else if(type == TokenType.UNTIL){
            procUntil();
        }
        else if(type == TokenType.FOR){
            procFor();
        }
        else if(type == TokenType.OUTPUT){ //Não existe Output
            procOutput();
        }
        else if(type == TokenType.ASSIGN){
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

    
}

