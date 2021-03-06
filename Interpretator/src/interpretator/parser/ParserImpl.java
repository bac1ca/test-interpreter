package interpretator.parser;

import interpretator.api.ast.AST;
import interpretator.api.ast.ASTKind;
import interpretator.api.ast.ExpressionAST;
import interpretator.api.ast.LambdaAST;
import interpretator.api.ast.Parser;
import interpretator.api.ast.ParserError;
import interpretator.api.ast.ProgramAST;
import interpretator.api.lexer.Lexer;
import interpretator.api.lexer.Token;
import interpretator.api.lexer.TokenKind;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alex
 */
/*package-local*/ final class ParserImpl implements Parser {

    private final List<Token> ts;
    private int index;
    private Token t;
    private final ProgramImpl program;
    private List<ParserError> errors = new ArrayList<>();

    /*package-local*/  ParserImpl(Lexer lexer) {
        ts = new ArrayList<>();
        while (true) {
            Token token = lexer.nextToken();
            switch (token.getKind()) {
                case Unknown:
                    addError("Unexpected token", token);
                    continue;
                case WhiteSpace:
                    continue;
            }
            ts.add(token);
            if (token.getKind() == TokenKind.EOF) {
                break;
            }
        }
        next();
        program = new ProgramImpl();
    }

    private void addError(String message, Token token) {
        errors.add(new ParserError(message, token.getTokenLine(), token.getStartRowCol(), token.getStartOffset(), token.getEndOffset()));
    }
    
    @Override
    public  List<ParserError> getErrors() {
        return errors;
    }
    
    private void next() {
        if (index < ts.size()) {
            t = ts.get(index++);
        }
    }

    @Override
    public ProgramAST parse() {
        while (true) {
            switch (t.getKind()) {
                case EOF:
                    return program;
                case Var:
                    var();
                    break;
                case Print:
                    print();
                    break;
                case Out:
                    out();
                    break;
                default:
                    addError("Unexpected token", t);
                    next();
                    break;
            }
        }
    }

    private void var() {
        Token startToken = t;
        next();
        if (t.getKind() == TokenKind.Identifier) {
            Token id = t;
            next();
            if (t.getKind() == TokenKind.Eq) {
                next();
                ExpressionAST expr = expression();
                program.add(new VarImpl(startToken, id, expr));
            } else {
                addError("Expected '='", t);
            }
        } else {
            addError("Expected identifier", t);
            //TODO: error
        }
    }

    private void print() {
        Token startToken = t;
        next();
        if (t.getKind() == TokenKind.String) {
            program.add(new PrintImpl(startToken, t));
        } else {
            addError("Expected string", t);
        }
        next();
    }

    private void out() {
        Token startToken = t;
        next();
        AST expr = expression();
        program.add(new OutImpl(startToken, expr));
    }

    private ExpressionAST expression() {
        return parsePlusMinus();
    }

    private ExpressionAST parsePlusMinus() {
        ExpressionAST lh = parseMulDiv();
        while (t.getKind() == TokenKind.Plus || t.getKind() == TokenKind.Minus) {
            Token op = t;
            next();
            ExpressionAST rh = parseMulDiv();
            lh = new BinaryExpressionImpl(lh, rh, op, op.getKind() == TokenKind.Plus ? ASTKind.Plus : ASTKind.Minus);
        }
        return lh;
    }

    private ExpressionAST parseMulDiv() {
        ExpressionAST lh = parsePow();
        while (t.getKind() == TokenKind.Mul || t.getKind() == TokenKind.Div) {
            Token op = t;
            next();
            ExpressionAST rh = parsePow();
            lh = new BinaryExpressionImpl(lh, rh, op, op.getKind() == TokenKind.Mul ? ASTKind.Mul : ASTKind.Div);
        }
        return lh;
    }

    private ExpressionAST parsePow() {
        ExpressionAST lh = parseUnaryMinus();
        //left associative ^
        //while (t.getKind() == TokenKind.Pow) {
        //    Token op = t;
        //    next();
        //    ExpressionAST rh = parseUnaryMinus();
        //    lh = new BinaryExpressionImpl(lh, rh, op, ASTKind.Pow);
        //}
        //right associative ^
        if (t.getKind() == TokenKind.Pow) {
            Token op = t;
            next();
            ExpressionAST rh = parsePow();
            lh = new BinaryExpressionImpl(lh, rh, op, ASTKind.Pow);
        }
        return lh;
    }
    
    private ExpressionAST parseUnaryMinus() {
        if (t.getKind() == TokenKind.Minus) {
            Token op = t;
            next();
            ExpressionAST rh = parseFunction();
            return new UnaryExpressionImpl(rh, op, ASTKind.UnaryMinus);
        }
        return parseFunction();
    }

    private ExpressionAST parseFunction() {
        switch (t.getKind()) {
            case Map:
                return parseMap();
            case Reduce:
                return parseReduce();
            case LParen: {
                next();
                ExpressionAST expr = expression();
                if (t.getKind() == TokenKind.RParen) {
                    next();
                    return expr;
                } else {
                    addError("Expected ')'", t);
                }
                break;
            }
            case LBrace: {
                Token startToken = t;
                next();
                ExpressionAST arg1 = expression();
                if (t.getKind() == TokenKind.Comma) {
                    next();
                    ExpressionAST arg2 = expression();
                    if (t.getKind() == TokenKind.RBrace) {
                        next();
                        return new SequenceImpl(startToken, arg1, arg2);
                    } else {
                        addError("Expected '}'", t);
                    }
                } else {
                    addError("Expected ','", t);
                }
                break;
            }
            case Identifier: {
                Token id = t;
                next();
                return new VariableImpl(id);
            }
            case Number: {
                Token number = t;
                next();
                return new NumberImpl(number);
            }
            default: {
                addError("Unexpected token", t);
                next();
            }
        }
        assert errors.size() > 0;
        return null;
    }

    private ExpressionAST parseMap() {
        Token map = t;
        next();
        if (t.getKind() == TokenKind.LParen) {
            next();
            ExpressionAST arg1 = expression();
            if (t.getKind() == TokenKind.Comma) {
                next();
                LambdaAST lambda = parseLambda();
                if (t.getKind() == TokenKind.RParen) {
                    next();
                    return new MapImpl(map, arg1, lambda);
                } else {
                    addError("Expected ')'", t);            
                }
            } else {
                addError("Expected ','", t);
            }
        } else {
            addError("Expected '('", t);
        }
        assert errors.size() > 0;
        return null;
    }

    private ExpressionAST parseReduce() {
        Token reduce = t;
        next();
        if (t.getKind() == TokenKind.LParen) {
            next();
            ExpressionAST arg1 = expression();
            if (t.getKind() == TokenKind.Comma) {
                next();
                ExpressionAST arg2 = expression();
                if (t.getKind() == TokenKind.Comma) {
                    next();
                    LambdaAST lambda = parseLambda();
                    if (t.getKind() == TokenKind.RParen) {
                        next();
                        return new ReduceImpl(reduce, arg1, arg2, lambda);
                    } else {
                        addError("Expected ')'", t);
                    }
                } else {
                    addError("Expected ','", t);
                }
            } else {
                addError("Expected ','", t);
            }
        } else {
            addError("Expected '('", t);
        }
        assert errors.size() > 0;
        return null;
    }

    private LambdaImpl parseLambda() {
        Token v1;
        Token v2 = null;
        if (t.getKind() == TokenKind.Identifier) {
            v1 = t;
            next();
            if (t.getKind() == TokenKind.Identifier) {
                v2 = t;
                next();
            }
            if (t.getKind() == TokenKind.Arrow) {
                next();
                ExpressionAST function = expression();
                return new LambdaImpl(v1, v2, function);
            } else {
                addError("Expected '->'", t);
            }
        } else {
            addError("Expected identifier", t);
        }
        assert errors.size() > 0;
        return null;
    }
}
