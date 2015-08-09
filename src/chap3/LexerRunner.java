package chap3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;

import stone.CodeDialog;
import stone.Lexer;
import stone.ParseException;
import stone.Token;

public class LexerRunner {
	public static void main(String[] args) throws ParseException, Exception {
//		Reader r=new CodeDialog();
		Reader r=new InputStreamReader(new FileInputStream("1.txt"));
		Lexer l=new Lexer(r);
		
		for(Token t;(t=l.read()) != Token.EOF ;)
			System.out.println("=>" + t.getText());
	}
}
