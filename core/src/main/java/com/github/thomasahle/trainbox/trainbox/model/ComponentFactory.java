package com.github.thomasahle.trainbox.trainbox.model;

import static playn.core.PlayN.log;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.StringTokenizer;

public final class ComponentFactory {
	enum Token {BOX("box"), CAT("cat"), DUP("dup"), ID("id"), FLIP("flip"),
				DOT(" "), MERG("||"), PA1("("), PA2(")");
		final String repr;
		Token(String repr) { this.repr = repr; }
	};
	
	/**
	 * @param description Maybe something like (dup dup || box)
	 * @return A component to use
	 */
	@SuppressWarnings("serial")
	private final static Map<Token, Class<? extends Component>> MAP =
			new HashMap<Token, Class<? extends Component>>() {{
		put(Token.BOX, BoxComponent.class);
		put(Token.CAT, ConcatComponent.class);
		put(Token.DUP, DupComponent.class);
		put(Token.ID, IdentityComponent.class);
		put(Token.FLIP, FlipComponent.class);
	}};
	
	/**
	 * Should fix "dup dup || cat" into "(dup dup)||cat" and
	 * "dup dup dup" into "((dup dup) dup)" and so on.
	 */
	private static String fixAsoc(String desc) {
		return "";
	}
	
	/**
	 * Parser with the following assertions:
	 * - Everything is parenthesized (no assoc)
	 * - There are no unused characters (e.g. whitespace)
	 * - No tokens representation is the prefix of another one
	 */
	public static Component parseStrictAlgebraic(Component e, String desc) {
		Queue<Token> inp = tokenize(desc+")");
		System.out.println("Token string: "+inp.toString());
		SynTree tree = parse(inp);
		System.out.println("Tree: "+tree);
		return tree.parse(e);
	}
	
	/**
	 * This one should be more easy going
	 * // flip (dup dup || box)
	 */
	public static Component parseAlgebraic(Component e, String desc) {
		// TODO
		return null;
	}
	
	private static Queue<Token> tokenize(String desc) {
		Queue<Token> tokens = new ArrayDeque<Token>();
		String current = "";
		for (char c : desc.toCharArray()) {
			current += c;
			for (Token tok : Token.values())
				if (tok.repr.equals(current)) {
					tokens.add(tok);
					current = "";
				}
		}
		return tokens;
	}
	
	private static interface SynTree {
		Component parse(Component e);
	}
	private static class OpNode implements SynTree {
		SynTree left, right;
		Token op;
		OpNode (SynTree left, Token op, SynTree right) {
			this.left = left; this.op = op; this.right = right;
		}
		public Component parse(Component e) {
			if (op == Token.DOT) {
				Component m = left.parse(e);
				return right.parse(m);
			}
			if (op == Token.MERG) {
				SplitComponents hooks = new SplitComponents(e);
				Component a = left.parse(hooks.fst);
				Component b = right.parse(hooks.snd);
				return new MergeComponent(a, b);
			}
			throw new IllegalArgumentException("Operator "+op+" is not expected");
		}
		public String toString() {
			return "OpNode("+left.toString()+", "+op+", "+right.toString()+")";
		}
	}
	private static class LeafNode implements SynTree {
		private Token type;
		LeafNode(Token tok) {
			type = tok;
		}
		public Component parse(Component e) {
			try {
				return MAP.get(type).getConstructor(Component.class).newInstance(e);
			} catch (IllegalArgumentException e1) {
				log().debug("This shouldn't happen", e1);
			} catch (SecurityException e1) {
				log().debug("This shouldn't happen", e1);
			} catch (InstantiationException e1) {
				log().debug("This shouldn't happen", e1);
			} catch (IllegalAccessException e1) {
				log().debug("This shouldn't happen", e1);
			} catch (InvocationTargetException e1) {
				log().debug("This shouldn't happen", e1);
			} catch (NoSuchMethodException e1) {
				log().debug("This shouldn't happen", e1);
			}
			return null;
		}
		public String toString() {
			return "LeafNode("+type+")";
		}
	}
	private static SynTree parse(Queue<Token> inp) {
		Token tok = inp.poll();
		SynTree a;
		if (tok == Token.PA1) {
			a = parse(inp);
		} else {
			a = new LeafNode(tok);
		}
		
		tok = inp.poll();
		Token op;
		if (tok == Token.PA2) {
			return a;
		} else {
			op = tok;
		}
		
		tok = inp.poll();
		SynTree b;
		if (tok == Token.PA1) {
			b = parse(inp);
		} else {
			b = new LeafNode(tok);
		}
		
		tok = inp.poll();
		if (tok != Token.PA2)
			throw new IllegalArgumentException("We need an end parenthesis here.");
		
		return new OpNode(a, op, b);
	}
	
	/**
	 * On the form "3 5 4-5"
	 * The left most drives first.
	 */
	public static List<Train> parseTrains(String description) {
		List<Train> trains = new ArrayList<Train>();
		for (String trainDesc : description.split(" +")) {
			Train train = Train.EMPTY;
			for (String cargoDesc : trainDesc.split("-")) {
				train = train.addLast(new Carriage(Integer.parseInt(cargoDesc)));
			}
			trains.add(train);
		}
		return trains;
	}
}
