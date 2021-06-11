package lexical;

import java.io.FileInputStream;
import java.io.PushbackInputStream;

public class LexicalAnalysis implements AutoCloseable {

    private int line;
    private SymbolTable st;
    private PushbackInputStream input;

    public LexicalAnalysis(String filename) {
        try {
            input = new PushbackInputStream(new FileInputStream(filename));
        } catch (Exception e) {
            throw new LexicalException("Unable to open file");
        }

        st = new SymbolTable();
        line = 1;
    }

    public void close() {
        try {
            input.close();
        } catch (Exception e) {
            throw new LexicalException("Unable to close file");
        }
    }

    public int getLine() {
        return this.line;
    }

    public Lexeme nextToken() {
        Lexeme lex = new Lexeme("", TokenType.END_OF_FILE);

        int state = 1;
        while (state != 12 && state != 13) {
            int c = getc();
            
            switch (state) {
                case 1:
                    if(c.EQUALS(' ')||c.EQUALS('\t')||c.EQUALS('\n')||c.EQUALS('\r')){
                        state=1;
                    }
                    else if(c.EQUALS('#')){
                        state=2;
                    }
                    else if(c.EQUALS('.')){
                        state=3;
                    }
                    else if(c.EQUALS('=')){
                        state=5;
                    }
                    else if(c.EQUALS('<')||c.EQUALS('>')){
                        state=6;
                    }
                    else if(c.EQUALS('*')){
                        state=7;
                    }
                    else if(c.EQUALS('!')){
                        state=8;
                    }
                    else if(Character.isLetter(c)||c.EQUALS('_')){
                        lex.token += (char)c;
                        state=9;
                    }
                    else if(Character.isDigit(c)){
                        lex.token += (char)c;
                        state=10;
                    }
                    else if(c.EQUALS("'")){
                        state=11;
                    }
                    if(c.EQUALS(';')||c.EQUALS(',')||
                        c.EQUALS('%')||c.EQUALS('/')||
                        c.EQUALS('[')||c.EQUALS(']')||
                        c.EQUALS('(')||c.EQUALS(')')||
                        c.EQUALS('+')||c.EQUALS('-')){
                        state=12;
                    }
                    break;
                case 2:
                    if(c.EQUALS('\n')){
                        state=1;
                    } 
                    else{
                        state=2;
                    }
                    break;
                case 3:
                    if(c.EQUALS('.')){
                        state=4;
                    }
                    else{
                        ungetc();
                        state=12;
                    }
                    break;
                case 4:
                    if(c.EQUALS('.')){
                        state=12;
                    }
                    else{
                        ungetc();
                        state=12;
                    }
                    break;
                case 5:
                    if(c.EQUALS('=')){
                        state=6;
                    }
                    else{
                        ungetc();
                        state=12;
                    }
                    break;
                case 6:
                    if(c.EQUALS('=')){
                        state=12;
                    }
                    else{
                        ungetc();
                        state=12;
                    }
                    break;
                case 7:
                    if(c.EQUALS('*')){
                        state=12;
                    }
                    else{
                        ungetc();
                        state=12;
                    }
                    break;
                case 8:
                    if(c.EQUALS('=')){
                        state=12;
                    }
                    break;
                case 9:
                    if(c.EQUALS('_')||Character.isLetter(c)||Character.isDigit(c)){
                        state=9;
                    } 
                    else{
                        ungetc(); 
                        state=12;
                    }
                    break;
                case 10:
                    if(Character.isDigit(c)){
                        state=10;
                    } 
                    else{
                        ungetc();
                        state=13;
                    }
                    break;
                case 11:
                    if(c.EQUALS("'")){
                        state=13;
                    } 
                    else{
                        state=11;
                    }
                    break;
                default:
                    throw new LexicalException("Unreachable");
            }
        }

        if (state == 12)
            lex.type = st.find(lex.token);

        return lex;
    }

    private int getc() {
        try {
            return input.read();
        } catch (Exception e) {
            throw new LexicalException("Unable to read file");
        }
    }

    private void ungetc(int c) {
        if (c != -1) {
            try {
                input.unread(c);
            } catch (Exception e) {
                throw new LexicalException("Unable to ungetc");
            }
        }
    }
}
