package interpretator.run;

import interpretator.api.run.DoubleValue;
import interpretator.api.run.IntegerValue;
import interpretator.api.run.Value;
import interpretator.editor.DocumentContext;
import interpretator.editor.Lexer;
import interpretator.parser.Parser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alex
 */
public class RunTest {

    public RunTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void eval2Mul2() {
        Lexer lexer = new Lexer(new DocumentContext("var res = 2*2"));
        Parser parser = new Parser(lexer);
        ASTEval run = new ASTEval(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res instanceof IntegerValue);
        assertEquals(4, ((IntegerValue) res).getInteger());
    }

    @Test
    public void eval2Plus2Mul2() {
        Lexer lexer = new Lexer(new DocumentContext("var res = 2+2*2"));
        Parser parser = new Parser(lexer);
        ASTEval run = new ASTEval(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res instanceof IntegerValue);
        assertEquals(6, ((IntegerValue) res).getInteger());
    }

    @Test
    public void evalMinus2Pow2Plus2Mul2() {
        Lexer lexer = new Lexer(new DocumentContext("var res = -2^2+2*2"));
        Parser parser = new Parser(lexer);
        ASTEval run = new ASTEval(parser.parse());
        run.run();
        Value res = run.getVariable("res");
        assertEquals(true, res instanceof IntegerValue);
        assertEquals(8, ((IntegerValue) res).getInteger());
    }

    @Test
    public void evalPi() {
        Lexer lexer = new Lexer(new DocumentContext(
                "var n = 10000\n" +
                "var sequence = map({0, n}, i -> (-1)^i / (2.0 * i + 1))\n" +
                "var pi = 4 * reduce(sequence, 0, x y -> x + y)\n" +
                "var delta = pi - 355/113"));
        Parser parser = new Parser(lexer);
        ASTEval run = new ASTEval(parser.parse());
        run.run();
        Value res = run.getVariable("delta");
        assertEquals(true, res instanceof DoubleValue);
        assertTrue(Math.abs(((DoubleValue) res).getDouble()) < 0.0001);
    }
    
}
