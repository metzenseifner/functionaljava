package komar.lee.jonathan.functionaljava.functionaljava.monads;

public interface Function<A, B> {

  /* Starting point: A -> B*/
  B apply(A arg);

  /**
   *  Intro new domain T; Compose A->B with T->A; f ° g(x)
   *
   *  T->A + A->B = T->B
   *
   *  Premise: A->B is True
   *           T->A is True (input argument)
   *         ∴ T->B
   *
   * Set Conceptual Order: T A B
   * because we return a transit from T to B
   */
  default <T> Function<T, B> compose(Function<T, A> g) {
    // Java executes f.apply, but is blocked until g is applied
    return x -> apply(g.apply(x));
  }

  /**
   *  Intro new domain T; Compose T->A with A->B; g ° f(x)
   *
   * B->T + A->B = A->T
   *
   * Premise: A->B is True
   *          B->T is True (input argument)
   *        ∴ A->T
   *
   * Set Conceptual Order: A B T
   * because we return a transit from A to T
   *
   * Plain English: If you can give me a path from Codomain to another Set,
   * I can give you a function from original Domain to that other Set
   */
  default <T> Function<A, T> andThen(Function<B, T> g) {
    // Java executes g.apply, but is blocked until f is applied
    return x -> g.apply(apply(x));
  }

  /* Identity property to make this type a monoid */
  static <T> Function<T, T> identity() {
    return t->t;
  }

  /* Utility: static compose variant */!
  static <T, A, B> Function<T, B> compose(Function<A, B> f, Function<T,A> g) {
    return x -> f.apply(g.apply(x));
  }

  /* Utility: static andThen variant */
  static <T, A, B> Function<A, T> andThen(Function<A,B> f, Function<B,T> g) {
    return x -> g.apply(f.apply(x));
  }

  /* */
  static <T, A, B> Function<A, T> andThen2(Function<A, B> f, Function<B, T> g) {
    return x -> g.compose(f).apply(x);
  }

  static <T, A, B> Function<A, T> andThen3(Function<A, B> f, Function<B, T> g) {
    return compose(g, f);
  }

  static <T, A, B> Function<Function<A, B>, Function<Function<B, T>, Function<A, T>>> compose() {
    return x -> y -> y.compose(x);
  }

  static <T, A, B> Function<Function<A, B>, Function<Function<T, A>, Function<T, B>>> andThen() {
    return x -> y -> y.andThen(x);
  }

  static <T, A, B> Function<Function<B, T>, Function<Function<A, B>, Function<A, T>>> higherCompose() {
    return f -> g -> x -> f.apply(g.apply(x));
  }

  static <T, A, B> Function<Function<A, B>, Function<Function<B, T>, Function<A, T>>> hgiherAndThen() {
    return f -> g -> z -> g.apply(f.apply(z));
  }

}
