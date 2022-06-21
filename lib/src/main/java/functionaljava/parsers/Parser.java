package functionaljava.parsers;

public abstract class Parser<A> {

    /* Disable public constructor despite being abstract for good measure */
    private Parser() {
    }

    private static class Success<A> extends Parser<A> {
        private final A value;
        private final int position;

        private Success(A value, int position) {
            super();
            this.value = value;
            this.position = position;
        }

    }

    private static class Failure<A> extends Parser<A>{
        private final RuntimeException exception;
        private final Tuple<Integer, Integer> position; // (line, column)

        private Failure(String errorMessage, int position) {
            super();
            this.exception = new IllegalStateException(errorMessage);
            this.position = position;
        }

        private Failure(Exception exception, int position) {
            this.exception = new IllegalStateException(exception);
            this.position = position;
        }
    }
}
